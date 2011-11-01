/*
 * The copyright holders of this work license this file to You under
 * the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.  You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.junit.contrib.scenario;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * <p>
 * The custom runner <code>ScenarioRunner</code> implements parameterized tests.
 * When running a parameterized test class, instances are created for the
 * cross-product of the test methods and the test data elements. That means each
 * test method will be run for each {@link Scenario}.
 * </p>
 * <h3>Building parameterized scenarios</h3>
 * <p>
 * To obtain a {@link ScenarioList} for the runner, create a static factory
 * method annotated with {@link Scenarios &#064;Scenarios}. See
 * <code>ScenarioList</code> for factory and helper methods. To test a simple
 * calculator you could write:
 * 
 * <pre>
 * &#064;RunWith(ScenarioRunner.class)
 * public static final class CalculatorTest {
 * 
 * 	&#064;Scenarios
 * 	public static ScenarioList scenarios() {
 * 		return ScenarioList.fromArray(new Object[][][] {
 * 				{ { &quot;add zero to zero is zero&quot; }, { 0, 0, 0 } },
 * 				{ { &quot;1+0=1&quot; }, { 1, 0, 1 } }, { { &quot;1+2=3&quot; }, { 1, 2, 3 } } });
 * 	}
 * 
 * 	private final Integer summand1;
 * 	private final Integer summand2;
 * 	private final Integer expectedSum;
 * 
 * 	public CalculatorTest(Integer s1, Integer s2, Integer sum) {
 * 		this.summand1 = s1;
 * 		this.summand2 = s2;
 * 		this.expectedSum = sum;
 * 	}
 * 
 * 	&#064;Test
 * 	public void shouldCalculateCorrectSum() {
 * 		Calculator c = new Calculator();
 * 		assertThat(c.sum(summand1, summand2), is(expectedSum));
 * 	}
 * }
 * </pre>
 * 
 * </p>
 * <h3>Building custom scenarios</h3>
 * <p>
 * Because {@link Scenario} is an interface you can implement scenarios
 * yourself. This can become handy if you have a lot of parameters or
 * expectations to verify. Please note that if you catch yourself writing a
 * custom scenario you'll usually write an integration or system test. Writing a
 * custom scenario for a unit test might be a sign for bad designed code. Maybe
 * you can avoid the complex scenario and refactor your code.
 * </p>
 * <p>
 * If you have your custom scenario class you can write your test:
 * 
 * <pre>
 * &#064;RunWith(ScenarioRunner.class)
 * public static final class MyScenarioTest {
 * 
 * 	&#064;Scenarios
 * 	public static ScenarioList scenarios() {
 * 		return ScenarioList.fromList(Arrays.asList(new Scenario[] {
 *        new MyScenario(1),
 *        new MyScenario(2),
 *        createReallyComplexScenario()
 *      });
 * 	}
 * 
 * 	private static MyScenario createReallyComplexScenario() {
 * 		// do complex stuff
 * 	}
 * 
 * 	private final MyScenario scenario;
 * 
 * 	public CalculatorTest(final MyScenario scenario) {
 * 		this.scenario = scenario;
 * 	}
 * 
 * 	&#064;Test
 * 	public void shouldDoWhatIWant() {
 * 		// implement test here with help of scenario
 * 	}
 * }
 * </pre>
 * 
 * </p>
 * <h3>Before, after and rules together with scenarios</h3>
 * <p>
 * When using <code>ScenarioRunner</code> together with &#064;Before,
 * &#064;After or together with rules and class rules, please note that:
 * <ul>
 * <li>class rules and &#064;BeforeClass will be executed exactly once before
 * any test method for any scenario will be called.</li>
 * <li>&#064;AfterClass will be executed exactly once after all test methods for
 * all scenerios have been called</li>
 * <li>rules, &#064;Before and &#064;After will be executed once for each
 * combinatin of test methods and scenarios (that means for each element in the
 * cross product of test methods and scenarios available).
 * </p>
 * 
 * 
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
public class ScenarioRunner extends Suite {

	private final ArrayList<Runner> runners = new ArrayList<Runner>();

	/**
	 * Internal Runner for Scenarios.
	 * 
	 * @author Stefan Penndorf <stefan@cyphoria.net>
	 */
	private static final class TestClassRunnerForScenarios extends
			BlockJUnit4ClassRunner {

		private final Scenario scenario;

		public TestClassRunnerForScenarios(final Class<?> klass,
				final Scenario testScenario) throws InitializationError {
			super(klass);
			this.scenario = testScenario;
		}

		@Override
		public Object createTest() throws Exception {
			final Constructor<?> constructor = getTestClass()
					.getOnlyConstructor();
			final Class<?>[] parameters = constructor.getParameterTypes();

			if (parameters.length == 1
					&& Scenario.class.isAssignableFrom(parameters[0])) {
				return constructor.newInstance(this.scenario);
			}

			if (this.scenario instanceof ParameterizedScenario) {
				return constructor
						.newInstance(((ParameterizedScenario) this.scenario)
								.getParameters());
			}
			
			throw new Exception("Expected Constructor with single Scenario "
					+ "argument when using custom Scenario "
					+ "implementation.");
		}

		@Override
		protected String getName() {
			return String.format("[%s]", this.scenario.getName());
		}

		@Override
		protected String testName(final FrameworkMethod method) {
			return String.format("%s[%s]", method.getName(),
					this.scenario.getName());
		}

		@Override
		protected void validateConstructor(List<Throwable> errors) {
			validateOnlyOneConstructor(errors);
		}

		@Override
		protected Statement classBlock(RunNotifier notifier) {
			return childrenInvoker(notifier);
		}

	}

	public ScenarioRunner(Class<?> klass) throws Throwable {
		super(klass, (List<Runner>) null);
		final TestClass testClass = getTestClass();
		ScenarioList scenarios = getScenarioList(testClass);
		for (Scenario scenario : scenarios) {
			final Runner runner = createRunner(testClass, scenario);
			runners.add(runner);
		}
	}

	private Runner createRunner(final TestClass testClass,
			final Scenario scenario) throws InitializationError {
		return new TestClassRunnerForScenarios(testClass.getJavaClass(),
				scenario);
	}

	private final ScenarioList getScenarioList(final TestClass testClass)
			throws Throwable {
		return (ScenarioList) getParametersMethod(testClass).invokeExplosively(
				null);
	}

	@Override
	protected List<Runner> getChildren() {
		return runners;
	}

	private final FrameworkMethod getParametersMethod(TestClass testClass)
			throws Exception {
		final List<FrameworkMethod> methods = testClass
				.getAnnotatedMethods(Scenarios.class);

		if (!methods.isEmpty()) {
			final FrameworkMethod potentialCandidate = methods.get(0);

			if (isPublicStatic(potentialCandidate)) {
				return potentialCandidate;
			}
		}

		throw new Exception(
				"ScenarioRunner: No public static @Scenarios method on class.");
	}

	private final boolean isPublicStatic(final FrameworkMethod method) {
		final int modifiers = method.getMethod().getModifiers();
		return Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
	}

}

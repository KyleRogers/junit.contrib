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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.InitializationError;

/**
 * 
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
public class ScenarioRunnerSystemTest {
	
	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	public static final class Calculator {
		public Integer sum(final Integer summand1, final Integer summand2) {
			return summand1 + summand2;
		}
	}

	@RunWith(ScenarioRunner.class)
	public static final class CalculatorScenarioSample {

		@Scenarios
		public static ScenarioList scenarios() {
			return ScenarioList.fromArray(new Object[][][] {
					{ { "0+0=0" }, { 0, 0, 0 } }, 
					{ { "1+0=1" }, { 1, 0, 1 } },
					{ { "1+2=3" }, { 1, 2, 3 } }, });
		}

		private final Integer summand1;
		private final Integer summand2;
		private final Integer sum;

		public CalculatorScenarioSample(Integer summand1, Integer summand2,
				Integer sum) {
			super();
			this.summand1 = summand1;
			this.summand2 = summand2;
			this.sum = sum;
		}

		@Test
		public void shouldCalculateCorrectSum() {
			Calculator c = new Calculator();
			assertThat(c.sum(summand1, summand2), is(sum));
		}

		@Test
		public void shouldSumSymmetrically() {
			Calculator c = new Calculator();
			assertThat(c.sum(summand2, summand1), is(sum));
		}

	}

	@Test
	public void shouldCountAllRunTests() throws Exception {
		final Result result = JUnitCore
				.runClasses(CalculatorScenarioSample.class);
		assertEquals(6, result.getRunCount());
	}

	@Test
	public void shouldCountAllTestsBeforeRun() throws Exception {
		final Runner runner = Request
				.aClass(CalculatorScenarioSample.class).getRunner();
		assertEquals(6, runner.testCount());
	}

	@Test
	public void shouldNamePlansCorrectly() throws Exception {
		final Runner runner = Request
				.aClass(CalculatorScenarioSample.class).getRunner();
		final Description description = runner.getDescription();

		assertEquals("[0+0=0]", description.getChildren().get(0)
				.getDisplayName());
	}

	@RunWith(ScenarioRunner.class)
	public static final class HasTestFailureScenario {

		@Scenarios
		public static ScenarioList scenarios() {
			return ScenarioList.fromArray(new Object[][][] { {
					{ "failingScenarioName" }, {} } });
		}

		public HasTestFailureScenario(final Scenario scenario) {
			super();
		}

		@Test
		public void shouldFailAlways() {
			fail();
		}
	}

	@Test
	public void shouldNameFailuresCorrectly() {
		final Result result = JUnitCore
				.runClasses(HasTestFailureScenario.class);
		assertEquals(String.format("shouldFailAlways[failingScenarioName](%s)",
				HasTestFailureScenario.class.getName()), result.getFailures()
				.get(0).getTestHeader());
	}

	@RunWith(ScenarioRunner.class)
	public static final class BeforeAndAfterClass {

		static final StringBuilder log = new StringBuilder();

		@Scenarios
		public static ScenarioList scenarios() {
			return ScenarioList.fromArray(new Object[][][] {
					{ { "failingScenarioName1" }, {} },
					{ { "failingScenarioName2" }, {} }, });
		}

		@AfterClass
		public static void afterClass() {
			log.append(")afterClass");
		}

		@BeforeClass
		public static void beforeClass() {
			log.append("beforeClass(");
		}

		private final Scenario scenario;

		public BeforeAndAfterClass(final Scenario scenario) {
			super();
			this.scenario = scenario;
		}

		@Before
		public void beforeTest() {
			log.append("(beforeTest");
		}

		@After
		public void afterTest() {
			log.append("afterTest)");
		}

		@Test
		public void shouldFailAlways() {
			log.append("-123-");
			assertNotNull(scenario);
		}

		@Test
		public void shouldPassAlways() {
			log.append("-123-");
			assertNotNull(scenario);
		}
	}

	@Test
	public void beforeAndAfterClassAreRun() {
		BeforeAndAfterClass.log.setLength(0);
		JUnitCore.runClasses(BeforeAndAfterClass.class);
		assertEquals(
				"beforeClass(" +
					"(beforeTest-123-afterTest)" +
					"(beforeTest-123-afterTest)" +
					"(beforeTest-123-afterTest)" +
					"(beforeTest-123-afterTest)" +
				")afterClass",
				BeforeAndAfterClass.log.toString());
	}

	
	@RunWith(ScenarioRunner.class)
	public static final class HasPrivateConstructor {

		@Scenarios
		public static ScenarioList scenarios() {
			return ScenarioList.fromArray(new Object[][][] {
					{ { "failingScenarioName1" }, { 1 } }
			});
		}
		
		private HasPrivateConstructor(final Integer param) {
			super();
		}

		@Test
		public void someTest() {
			// succeeds... but never called
		}
	}
	
	@Test
	public void shouldFailWhenPrivateConstructorProvided() throws Throwable {
		expectedException.expect(InitializationError.class);

		new ScenarioRunner(HasPrivateConstructor.class);
	}	

	@RunWith(ScenarioRunner.class)
	public static final class HasNoArgConstructor {

		@Scenarios
		public static ScenarioList scenarios() {
			return ScenarioList.fromArray(new Object[][][] {
					{ { "failingScenarioName1" }, {} }
			});
		}
		
		public HasNoArgConstructor() {
			super();
		}

		@Test
		public void someTest() {
			// succeeds... but never called
		}
	}
	
	@Test
	public void shouldAllowPublicNoArgConstructor() throws Throwable {
		final Result result = JUnitCore.runClasses(HasNoArgConstructor.class);

		assertEquals(1, result.getRunCount());
		assertEquals(0, result.getIgnoreCount());
		assertEquals(0, result.getFailureCount());
	}
	
	@RunWith(ScenarioRunner.class)
	public static final class NoScenarioFactoryMethod {
		
		@Test
		public void neverCalled() {		}
	}
	
	@Test
	public void shouldFailIfNoScenariosAnnotationGiven() throws Throwable {
		expectedException.expectMessage("No public static @Scenarios method on class.");
		
		new ScenarioRunner(NoScenarioFactoryMethod.class);
	}
	
	@RunWith(ScenarioRunner.class)
	public static final class PrivateScenarioFactoryMethod {
		
		@SuppressWarnings("unused")
		@Scenarios
		private static ScenarioList scenarios() {
			return null;
		}
		
		@Test
		public void neverCalled() {		}
	}

	@Test
	public void shouldFailIfFactoryMethodIsPrivate() throws Throwable {
		expectedException.expectMessage("No public static @Scenarios method on class.");
		new ScenarioRunner(PrivateScenarioFactoryMethod.class);
	}

	@RunWith(ScenarioRunner.class)
	public static final class NonStaticScenarioFactoryMethod {
		
		@Scenarios
		public ScenarioList scenarios() {
			return null;
		}
		
		@Test
		public void neverCalled() {		}
	}

	@Test
	public void shouldFailIfFactoryMethodIsNotStatic() throws Throwable {
		expectedException.expectMessage("No public static @Scenarios method on class.");
		new ScenarioRunner(NonStaticScenarioFactoryMethod.class);
	}

	public static final class CustomScenario implements Scenario {
		private final String name;

		public CustomScenario(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}
	
	@RunWith(ScenarioRunner.class)
	public static final class WithCustomScenario {
		
		@Scenarios
		public static ScenarioList scenarios() {
			return ScenarioList.fromList(Arrays.asList(new Scenario[] {
					new CustomScenario("foo")
			}));
		}
		
		private final CustomScenario scenario;
		
		public WithCustomScenario(final CustomScenario scenario) {
			this.scenario = scenario;
		}
		
		@Test
		public void nameSupplied() {
			assertThat(scenario.getName(), is("foo"));
		}
	}

	@Test
	public void shouldSupportCustomScenarios() throws Throwable {
		final List<Failure> emptyList = Collections.emptyList();
		final Result result = JUnitCore.runClasses(WithCustomScenario.class);

		assertEquals("run", 1, result.getRunCount());
		assertThat(result.getFailures(), is(equalTo(emptyList)));
	}

	@RunWith(ScenarioRunner.class)
	public static class ConstructorInvalidForCustomScenario {
		
		@Scenarios
		public static ScenarioList customScenarios() {
			return ScenarioList.fromList(Arrays
					.asList(new CustomScenario("foo")));
		}
		
		public ConstructorInvalidForCustomScenario(final String param1) {
			super();
		}
		
		@Test
		public void neverCalled() {}
	}
	
	@Test
	public void shouldFailIfTestConstructorArgumentsDoNotMatchScenario()
			throws Throwable {
		final Result result = JUnitCore
				.runClasses(ConstructorInvalidForCustomScenario.class);
		assertThat(result.getFailureCount(), is(1));
		assertThat(result.getFailures().get(0).getMessage(),
				containsString("Expected Constructor with single Scenario "
						+ "argument when using custom Scenario "
						+ "implementation."));
	}
	
	@RunWith(ScenarioRunner.class)
	public static final class HasScenarioWithBadName {
		
		@Scenarios
		public static ScenarioList scenarios() {
			return ScenarioList.fromArray(new Object[][][] {
					{ {"with()parantheses"}, {} }
			});
		}
		
		@Rule
		public final TestName testName = new TestName(); 
		
		public HasScenarioWithBadName() {
			super();
		}
		
		@Test
		public void shouldReportCorrectTestNameEvenWithParantheses() {
			assertThat(
					testName.getMethodName(),
					is(equalTo("shouldReportCorrectTestNameEvenWithParantheses[with()parantheses]")));
		}

	}
	
	@Test
	public void shouldSupportParanthesesInScenarioName()
			throws Throwable {
		List<Failure> noFailures = Collections.emptyList();
		Result r = JUnitCore.runClasses(HasScenarioWithBadName.class);
		assertThat(r.getFailures(), is(equalTo(noFailures)));
	}
}

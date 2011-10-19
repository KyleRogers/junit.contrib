/**
 * 
 */
package org.junit.contrib.scenario;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * 
 * @author Stefan Penndorf
 */
public class ScenarioRunner extends Suite {

	private final ArrayList<Runner> runners = new ArrayList<Runner>();

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Scenarios {

	}

	private final class TestClassRunnerForScenarios extends
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

			if (parameters.length == 0) {
				return constructor.newInstance();
			}
			
			if(parameters.length == 1 && Scenario.class.isAssignableFrom(parameters[0])) {
				return constructor.newInstance(this.scenario);
			}

			if (this.scenario instanceof ParameterizedScenario) {
				return constructor
						.newInstance(((ParameterizedScenario) this.scenario)
								.getParameters());
			}
			
			return null;
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
		ScenarioList scenarios = getScenarioMap(testClass);
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

	private final ScenarioList getScenarioMap(final TestClass testClass)
			throws Throwable {
		return (ScenarioList) getParametersMethod(testClass).invokeExplosively(
				null);
	}

	@Override
	protected List<Runner> getChildren() {
		return runners;
	}

	private FrameworkMethod getParametersMethod(TestClass testClass)
			throws Exception {
		final List<FrameworkMethod> methods = testClass
				.getAnnotatedMethods(Scenarios.class);
		return methods.get(0);
	}
	
}

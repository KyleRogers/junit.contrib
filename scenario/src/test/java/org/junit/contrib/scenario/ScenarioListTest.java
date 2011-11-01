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

import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
@RunWith(Enclosed.class)
public class ScenarioListTest {
	
	public static void assertEmpty(ScenarioList list) {
		assertThat("List created may not be null", list,
				is(notNullValue(ScenarioList.class)));
		assertThat(list.iterator().hasNext(),
				describedAs("scenario list is empty", is(false)));
	}
	
	public static class ScenarioListFromArrayTest {
	
		@Test
		public void shouldBuildFromEmptyArray() {
			assertEmpty(ScenarioList.fromArray(new Object[][][] {}));
		}

		@Test
		public void shouldBuildSingleScenario() {
			final ParameterizedScenario scenario = new ParameterizedScenario(
					"foo", 1, 2, 3, new Object());
			final ScenarioList result = ScenarioList
					.fromArray(new Object[][][] { { 
						{ "foo" }, scenario.getParameters() 
					}, });
			
			assertThat(result.iterator().next(), is((Scenario) scenario));
		}
	
		@Test
		public void shouldBuildMultipleScenariosAndRetainOrder() {
			final ParameterizedScenario scenario1 = new ParameterizedScenario(
					"foo1", 1, 2, 3, new Object());
			final ParameterizedScenario scenario2 = new ParameterizedScenario(
					"foo2", new Object());
			final ParameterizedScenario scenario3 = new ParameterizedScenario(
					"foo3", 2, null, new Object());
	
			final ScenarioList result = ScenarioList.fromArray(new Object[][][] {
					{ { scenario1.getName() }, scenario1.getParameters() },
					{ { scenario2.getName() }, scenario2.getParameters() },
					{ { scenario3.getName() }, scenario3.getParameters() }, });
	
			final Iterator<Scenario> scenarios = result.iterator();
			assertThat(scenarios.next(), is((Scenario) scenario1));
			assertThat(scenarios.next(), is((Scenario) scenario2));
			assertThat(scenarios.next(), is((Scenario) scenario3));
		}
	}
	
	private static class CustomScenario implements Scenario {
		private final String name;

		private CustomScenario(final String name) {
			super();
			this.name = name;
		}
		public String getName() {
			return this.name;
		}
	}
	
	public static class ScenarioListFromListTest {
		
		@Test
		public void shouldBuildFromEmptyList() {
			List<CustomScenario> emptyList = Collections.emptyList();
			assertEmpty(ScenarioList.fromList(emptyList));
		}

		@Test
		public void shouldBuildSingleScenario() {
			final CustomScenario scenario = new CustomScenario("foo");
			
			final ScenarioList result = ScenarioList
					.fromList(Arrays.asList(scenario));
			
			assertThat(result.iterator().next(), is((Scenario) scenario));
		}
	
		@Test
		public void shouldBuildMultipleScenariosAndRetainOrder() {
			final CustomScenario scenario1 = new CustomScenario("foo1");
			final CustomScenario scenario2 = new CustomScenario("foo2");
			final CustomScenario scenario3 = new CustomScenario("foo3");
	
			final List<CustomScenario> scenarios = Arrays.asList(scenario1,
					scenario2, scenario3);			
			
			final ScenarioList result = ScenarioList.fromList(scenarios);
	
			final Iterator<Scenario> it = result.iterator();
			assertThat(it.next(), is((Scenario) scenario1));
			assertThat(it.next(), is((Scenario) scenario2));
			assertThat(it.next(), is((Scenario) scenario3));
		}
	}
		
}

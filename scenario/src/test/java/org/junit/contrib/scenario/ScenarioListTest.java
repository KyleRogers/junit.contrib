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

import java.util.Iterator;

import org.junit.Test;

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
public class ScenarioListTest {

	@Test
	public void shouldBuildFromEmptyArray() {
		final ScenarioList result = ScenarioList.fromArray(new Object[0][0][0]);
		assertThat(result, is(notNullValue(ScenarioList.class)));
		assertThat(result.iterator().hasNext(),
				describedAs("scenario list is empty", is(false)));
	}

	@Test
	public void shouldBuildSingleScenario() {
		final ParameterizedScenario scenario = new ParameterizedScenario("foo",
				1, 2, 3, new Object());
		final ScenarioList result = ScenarioList.fromArray(new Object[][][] { {
				{ "foo" }, scenario.getParameters() }, });
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

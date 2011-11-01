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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A <code>ScenarioList</code> is an order read-only list of scenarios. The
 * <code>ScenarioList</code> class provides various factory methods for
 * convenient creation from custom scenario collections or object arrays.
 * 
 * 
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
public final class ScenarioList implements Iterable<Scenario> {

	/**
	 * <p>
	 * Creates a new {@link ScenarioList} from the 3-dimensional array provided.
	 * The first (outermost) dimension will split the different scenarios, the
	 * second dimension will split scenario name and scenario parameters and the
	 * third contains scenario name or scenario parameters. For example:
	 * 
	 * <pre>
	 * ScenarioList.fromArray(new Object[][][] {
	 *  { { &quot;add zero to zero is zero&quot; }, { 0, 0, 0 } },
	 *  { { &quot;1+0=1&quot; }, { 1, 0, 1 } }, 
	 *  { { &quot;1+2=3&quot; }, { 1, 2, 3 } }
	 * });
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param arrs
	 *            array with scenario names and scenario parameters.
	 * @return the <code>ScenarioList</code> created from <code>arrs</code>.
	 */
	public static ScenarioList fromArray(final Object[][][] arrs) {
		final ArrayList<Scenario> scenarios = new ArrayList<Scenario>(
				arrs.length);

		for (Object[][] arr : arrs) {
			final ParameterizedScenario scenario = new ParameterizedScenario(
					(String) arr[0][0], arr[1]);
			scenarios.add(scenario);
		}

		return new ScenarioList(scenarios);
	}

	/**
	 * <p>
	 * Creates a new {@link ScenarioList} from the collection of scenarios
	 * provided. The scenarios will be managed in the order they are returned by
	 * the collection's iterator. This will be most likely the order in which
	 * those scenarios get executed. For example:
	 * 
	 * <pre>
	 * List&lt;MyScenario&gt; scenarios = new ArrayList&lt;MyScenario&gt;();
	 * scenarios.add(...);
	 * ...
	 * 
	 * ScenarioList.fromList(scenarios);
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param scenarioCollection
	 *            collection of scenarios used to create a new
	 *            <code>ScenarioList</code>.
	 * @return the <code>ScenarioList</code> created from
	 *         <code>scenarioCollection</code>.
	 */
	public static ScenarioList fromList(
			Collection<? extends Scenario> scenarioCollection) {
		return new ScenarioList(scenarioCollection);
	}

	private final List<Scenario> scenarios;

	/**
	 * Private constructor, see static factory methods.
	 * 
	 * @param scenarios
	 *            collection of scenarios.
	 */
	private ScenarioList(final Collection<? extends Scenario> scenarios) {
		super();
		this.scenarios = new ArrayList<Scenario>(scenarios);
	}

	/**
	 * Returns an iterator over the elements of this <code>ScenarioList</code>
	 * in proper sequence.
	 */
	public Iterator<Scenario> iterator() {
		return scenarios.iterator();
	}

}

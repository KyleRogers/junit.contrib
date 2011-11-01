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
 * convenient creation from custom scenarios or object arrays.
 * 
 * 
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
public class ScenarioList implements Iterable<Scenario> {

	private final List<Scenario> scenarios;

	protected ScenarioList(final Collection<Scenario> scenarios) {
		super();
		this.scenarios = new ArrayList<Scenario>(scenarios);
	}

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
	 *            array with scenario names and scenario paramters.
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

	public Iterator<Scenario> iterator() {
		return scenarios.iterator();
	}

	/**
	 * @param asList
	 * @return
	 */
	public static ScenarioList fromList(List<Scenario> asList) {
		return new ScenarioList(null);
	}

}

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

/**
 * <p>
 * A <code>Scenario</code> describes a parameterized run of a test
 * specification. A <code>Scenario</code> might specify the parameters
 * characteristically for that run as well as the expected result or outcome.
 * <code>Scenario</code>s of the same type but parameterized differently will be
 * used with the same set of test methods.
 * </p>
 * <p>
 * See {@link ScenarioRunner} for additional information and examples.
 * </p>
 * 
 * 
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
public interface Scenario {

	/**
	 * Returns the name of the scenario describing the purpose or set of input
	 * parameters of the specific run. Each scenario should have a descriptive
	 * and human readable name.
	 * 
	 * @return the human readable name of the scenario.
	 */
	public String getName();

}

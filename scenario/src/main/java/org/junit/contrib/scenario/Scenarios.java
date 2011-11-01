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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation for a method which provides parameters to be injected into the
 * test class constructor by <code>ScenarioRunner</code>. The methods annotated
 * must
 * <ul>
 * <li>be public and static</li>
 * <li>take no parameters</li>
 * <li>return an instance of {@link ScenarioList}</li>
 * </ul>
 * </p>
 * 
 * 
 * @author Stefan Penndorf <stefan@cyphoria.net>
 * @see ScenarioRunner
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Scenarios {

}
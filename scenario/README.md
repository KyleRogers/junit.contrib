Welcome to JUnit Contrib Scenario
=================================

This JUnit extension extends the JUnit library with the concept of scenarios.
Scenarios can be used to specify complex setups and verifications intended to be
used in for JUnit based system and intergration tests. 

**This is an alpha release. The package names, API, and features may change in
 a future release.** 

---

Basic Usage
-----------
The scenario was inspired by JUnits built-in `Parameterized` runner.

Using this extension can be done in two steps:
* Declaring the custom `ScenarioRunner`
* Writing a static Scenario factory method annotated with `@Scenarios`

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ {.java}
 @RunWith(ScenarioRunner.class)
 public static final class CalculatorTest {
 
 	@Scenarios
 	public static ScenarioList scenarios() {
 		return ScenarioList.fromArray(new Object[][][] {
 				{ { "add zero to zero is zero" }, { 0, 0, 0 } },
 				{ { "1+0=1" }, { 1, 0, 1 } }, 
 				{ { "1+2=3" }, { 1, 2, 3 } } });
 	}
 
 	private final Integer summand1;
 	private final Integer summand2;
 	private final Integer expectedSum;
 
 	public CalculatorTest(Integer s1, Integer s2, Integer sum) {
 		this.summand1 = s1;
 		this.summand2 = s2;
 		this.expectedSum = sum;
 	}
 
 	@Test
 	public void shouldCalculateCorrectSum() {
 		Calculator c = new Calculator();
 		assertThat(c.sum(summand1, summand2), is(expectedSum));
 	}
 }
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Using custom `Scenario`s
------------------------

Using before, after and rules together with scenarios
-----------------------------------------------------

Examples
--------

License
-------
This project uses the [Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).


Known Problems
--------------

Actually there are no known problems. 

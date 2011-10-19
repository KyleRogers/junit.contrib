package org.junit.contrib.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ScenarioList implements Iterable<Scenario> {

	private final List<Scenario> scenarios;

	protected ScenarioList(final Collection<Scenario> scenarios) {
		super();
		this.scenarios = new ArrayList<Scenario>(scenarios);
	}

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

}

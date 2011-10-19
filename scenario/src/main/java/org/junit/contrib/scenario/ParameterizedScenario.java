package org.junit.contrib.scenario;

import java.util.Arrays;

public class ParameterizedScenario implements Scenario {

	private final String name;
	private final Object[] parameters;

	public ParameterizedScenario(final String name, final Object... parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
	}

	public String getName() {
		return this.name;
	}
	
	Object[] getParameters() {
		return this.parameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = name.hashCode();
		result = prime * result + Arrays.hashCode(parameters);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ParameterizedScenario other = (ParameterizedScenario) obj;
		if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(parameters, other.parameters))
			return false;
		return true;
	}
	
	

}

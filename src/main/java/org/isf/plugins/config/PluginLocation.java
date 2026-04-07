package org.isf.plugins.config;

public enum PluginLocation {
	MAIN("main"), PATIENT("patient");
	final String value;

	PluginLocation(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}

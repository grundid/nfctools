package org.nfctools.llcp.parameter;

public class ServiceName {

	private String name;

	public ServiceName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "ServiceName: " + name;
	}
}

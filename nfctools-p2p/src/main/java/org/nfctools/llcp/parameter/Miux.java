package org.nfctools.llcp.parameter;

public class Miux {

	private int value;

	public Miux(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Miux: " + value;
	}

}

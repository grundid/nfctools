package org.nfctools.spi.arygon;

public class ArygonConnectionToken {

	private int targetNumber;

	public ArygonConnectionToken(int targetNumber) {
		this.targetNumber = targetNumber;
	}

	public int getTargetNumber() {
		return targetNumber;
	}

}

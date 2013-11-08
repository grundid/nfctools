package org.nfctools.spi.tama.response;

public class InListPassiveTargetResp {

	private int numberOfTargets;
	private byte[] targetData;

	public InListPassiveTargetResp(int numberOfTargets, byte[] targetData) {
		this.numberOfTargets = numberOfTargets;
		this.targetData = targetData;
	}

	public int getNumberOfTargets() {
		return numberOfTargets;
	}

	public byte[] getTargetData() {
		return targetData;
	}

	public byte getTargetId() {
		return targetData[0];
	}

	public byte getSelectResponse() {
		return targetData[3];
	}

	public boolean isIsoDepSupported() {
		return (getSelectResponse() & 0x20) == 0x20;
	}
}

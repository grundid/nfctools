package org.nfctools.spi.tama.request;

public class InListPassiveTargetReq {

	private byte maxTargets;
	private byte bateRate;
	private byte[] initiatorData;

	public InListPassiveTargetReq(byte maxTargets, byte bateRate, byte[] initiatorData) {
		this.maxTargets = maxTargets;
		this.bateRate = bateRate;
		this.initiatorData = initiatorData;
	}

	public byte getMaxTargets() {
		return maxTargets;
	}

	public byte getBateRate() {
		return bateRate;
	}

	public byte[] getInitiatorData() {
		return initiatorData;
	}

	public int getInitiatorDataLength() {
		return hasInitiatorData() ? initiatorData.length : 0;
	}

	public boolean hasInitiatorData() {
		return initiatorData != null;
	}
}

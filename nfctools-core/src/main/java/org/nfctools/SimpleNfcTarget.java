package org.nfctools;

import org.nfctools.api.Target;

public class SimpleNfcTarget implements Target {

	private int mode;
	private byte[] nfcId;
	private byte[] generalBytes;

	public SimpleNfcTarget(int mode, byte[] nfcId, byte[] generalBytes) {
		this.mode = mode;
		this.nfcId = nfcId;
		this.generalBytes = generalBytes;
	}

	@Override
	public int getMode() {
		return mode;
	}

	@Override
	public byte[] getNfcId() {
		return nfcId;
	}

	@Override
	public byte[] getGeneralBytes() {
		return generalBytes;
	}
}

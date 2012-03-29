package org.nfctools.spi.scm;

public class ConnectResponse {

	private int result;

	private int mode;
	private byte[] nfcId;
	private byte[] generalBytes;

	public ConnectResponse(int result) {
		this.result = result;
	}

	public ConnectResponse(int mode, byte[] nfcId, byte[] generalBytes) {
		this.mode = mode;
		this.nfcId = nfcId;
		this.generalBytes = generalBytes;
	}

	public int getResult() {
		return result;
	}

	public int getMode() {
		return mode;
	}

	public byte[] getNfcId() {
		return nfcId;
	}

	public byte[] getGeneralBytes() {
		return generalBytes;
	}

}

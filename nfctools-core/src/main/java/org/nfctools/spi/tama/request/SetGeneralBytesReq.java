package org.nfctools.spi.tama.request;

public class SetGeneralBytesReq {

	private byte[] generalBytes;

	public SetGeneralBytesReq(byte[] generalBytes) {
		this.generalBytes = generalBytes;
	}

	public byte[] getGeneralBytes() {
		return generalBytes;
	}
}

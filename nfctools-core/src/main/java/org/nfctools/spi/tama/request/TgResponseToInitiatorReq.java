package org.nfctools.spi.tama.request;

public class TgResponseToInitiatorReq extends SetDepDataReq {

	public TgResponseToInitiatorReq(byte[] dataOut, int offset, int length) {
		super(dataOut, offset, length);
	}

}

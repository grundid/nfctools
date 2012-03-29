package org.nfctools.spi.tama.request;

@SuppressWarnings("unused")
public class SetParametersReq {

	private static final int NAD_USED = 0x01;
	private static final int DID_USED = 0x02;
	private static final int AUTOMATIC_ATR_RES = 0x04;
	private static final int TDA_POWERED = 0x08;
	private static final int AUTOMATIC_RATS = 0x10;
	private static final int SECURE = 0x20;

	private int flags = AUTOMATIC_ATR_RES | AUTOMATIC_RATS;

	public SetParametersReq setAutomaticATR_RES(boolean value) {
		if (value) {
			flags |= AUTOMATIC_ATR_RES;
		}
		else {
			flags &= ~AUTOMATIC_ATR_RES;
		}
		return this;
	}

	public byte getFlags() {
		return (byte)flags;
	}

}

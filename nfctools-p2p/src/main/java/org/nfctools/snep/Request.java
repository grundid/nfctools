package org.nfctools.snep;

public enum Request {

	CONTINUE(0x00), GET(0x01), PUT(0x02), REJECT(0x7f);

	private Request(int code) {
		this.code = (byte)code;
	}

	byte code;

	public byte getCode() {
		return code;
	}
}

package org.nfctools.snep;

public enum Response {

	CONTINUE(0x80), SUCCESS(0x81), NOT_FOUND(0xC0), EXCESS_DATA(0xC1), BAD_REQUEST(0xC2), NOT_IMPLEMENTED(0xE0), UNSUPPORTED_VERSION(
			0xE1), REJECT(0xFF);

	byte code;

	private Response(int code) {
		this.code = (byte)code;
	}

	public byte getCode() {
		return code;
	}

}

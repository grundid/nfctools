package org.nfctools.scio;

public class Response {

	private int sw1;
	private int sw2;
	private byte[] data;

	public Response(int sw1, int sw2, byte[] data) {
		this.sw1 = sw1;
		this.sw2 = sw2;
		this.data = data;
	}

	public int getSw1() {
		return sw1;
	}

	public int getSw2() {
		return sw2;
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public String toString() {
		return "SW1: " + sw1 + " SW2: " + sw2;
	}

}

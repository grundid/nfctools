package org.nfctools.snep;

import java.nio.ByteBuffer;

public class SnepMessage {

	private byte version;
	private byte messageCode;
	private long length;
	private byte[] information;

	public SnepMessage(byte[] message) {
		ByteBuffer buffer = ByteBuffer.wrap(message);
		version = buffer.get();
		messageCode = buffer.get();
		length = buffer.getInt();
		information = new byte[(int)length];
		buffer.get(information);
	}

	public SnepMessage(byte version, byte messageCode) {
		this.version = version;
		this.messageCode = messageCode;
	}

	public SnepMessage(int version, Request request) {
		this.version = (byte)version;
		this.messageCode = request.getCode();
	}

	public SnepMessage(int version, Response response) {
		this.version = (byte)version;
		this.messageCode = response.getCode();
	}

	public void setInformation(byte[] information) {
		this.information = information;
		this.length = information.length;
	}

	public byte[] getInformation() {
		return information;
	}

	public byte getMessageCode() {
		return messageCode;
	}

	public byte getVersion() {
		return version;
	}

	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate((int)(6 + length));

		buffer.put(version);
		buffer.put(messageCode);
		buffer.putInt((int)length);
		if (information != null)
			buffer.put(information);

		return buffer.array();
	}

}

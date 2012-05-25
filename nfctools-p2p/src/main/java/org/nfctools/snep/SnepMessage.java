/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

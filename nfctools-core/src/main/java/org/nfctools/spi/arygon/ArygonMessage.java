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
package org.nfctools.spi.arygon;


public class ArygonMessage {

	private byte[] header;
	private byte[] payload;

	public ArygonMessage(byte[] payload) {
		this.payload = payload;
	}

	public ArygonMessage(byte[] header, byte[] payload) {
		this.header = header;
		this.payload = payload;
	}

	public byte[] getPayload() {
		return payload;
	}

	public byte[] getHeader() {
		return header;
	}

	public boolean hasPayload() {
		return payload != null && payload.length > 0;
	}

	public boolean hasHeader() {
		return header != null && header.length > 0;
	}

	public boolean hasErrorCodes() {
		return getErrorCode1() != 0x00 || getErrorCode2() != 0x00;
	}

	public byte getErrorCode1() {
		return header[1];
	}

	public byte getErrorCode2() {
		return header[2];
	}

	public boolean hasTamaErrorCode() {
		return (payload.length < 4 || payload[2] != '0' || payload[3] != '0');
	}

	public byte getTamaErrorCode() {
		String code = new String(payload, 2, 2);
		return Integer.valueOf(code, 16).byteValue();
	}
}

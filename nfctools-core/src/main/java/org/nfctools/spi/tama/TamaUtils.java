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
package org.nfctools.spi.tama;

public class TamaUtils {

	public static void handleStatusCode(int status) throws TamaException {
		if (TamaUtils.isError(status))
			throw new TamaException(TamaUtils.getErrorCodeFromStatus(status));
	}

	public static boolean isError(int status) {
		return getErrorCodeFromStatus(status) != 0;
	}

	public static int getErrorCodeFromStatus(int status) {
		return status & 0x3f;
	}

	public static boolean isMoreInformation(int status) {
		return (status & 0x40) == 0x40;
	}

	public static boolean isNADPresent(int status) {
		return (status & 0x80) == 0x80;
	}

	static boolean isACKFrame(byte[] frame, int offset) {
		boolean ackFrame = (frame.length + offset >= 6) && (frame[offset] == 0x00) && (frame[offset + 1] == 0x00)
				&& (frame[offset + 2] == (byte)0xFF) && (frame[offset + 3] == 0x00)
				&& (frame[offset + 4] == (byte)0xFF) && (frame[offset + 5] == 0x00);
		return ackFrame;
	}

	static boolean isErrorFrame(byte[] frame, int offset) {
		return (frame.length + offset >= 8 && (frame[offset] == 0x00) && (frame[offset + 1] == 0x00)
				&& (frame[offset + 2] == (byte)0xFF) && (frame[offset + 3] == 0x01) && (frame[offset + 4] == (byte)0xFF));
	}

	public static byte[] unpackPayload(byte[] message) {
		int length = message[3] & 0xff;
		byte[] payload = new byte[length];
		System.arraycopy(message, 5, payload, 0, payload.length);
		return payload;
	}

}

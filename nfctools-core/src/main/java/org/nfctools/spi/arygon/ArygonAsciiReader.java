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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.nfctools.utils.NfcUtils;

/**
 * Reads messages from an InputStream which are terminated with CRLF.
 * 
 */
public class ArygonAsciiReader {

	private static final long TIMEOUT_IN_MILLIS = 2000;
	private InputStream in;
	private byte[] buffer;
	private int bufPos = 0;

	public ArygonAsciiReader(InputStream in, int bufferSize) {
		this.in = in;
		this.buffer = new byte[bufferSize];
	}

	public ArygonMessage readResponse() throws IOException {
		long timeoutCounter = System.currentTimeMillis();
		while (System.currentTimeMillis() - timeoutCounter < TIMEOUT_IN_MILLIS) {
			int read = in.read(buffer, bufPos, buffer.length - bufPos);
			if (read != -1) {
				bufPos += read;
				timeoutCounter = System.currentTimeMillis();
			}
			else {
				try {
					Thread.sleep(1);
				}
				catch (InterruptedException e) {
					break;
				}
			}

			if (bufPos >= 3) // we need at least 3 characters to identify a message
			{
				for (int x = 1; x < bufPos; x++) {
					if (buffer[x - 1] == '\r' && buffer[x] == '\n') {
						byte[] resp = new byte[x - 1];
						System.arraycopy(buffer, 0, resp, 0, x - 1);
						System.arraycopy(buffer, x + 1, buffer, 0, bufPos - (x + 1));
						bufPos -= (x + 1);
						Arrays.fill(buffer, bufPos, buffer.length - bufPos, (byte)0);
						return createArygonMessage(resp);
					}
				}
			}
		}
		return new ArygonMessage(new byte[0]); // TODO vielleicht besser eine Timeout exception werfen 
	}

	private ArygonMessage createArygonMessage(byte[] bytes) throws IOException {
		if (bytes.length >= 8 && bytes[0] == 'F' && bytes[1] == 'F') {
			byte[] header = NfcUtils.convertHexAsciiToByteArray(bytes, 0, 8);

			byte[] payload = new byte[header[3]];
			try {
				System.arraycopy(bytes, 8, payload, 0, payload.length);
				return new ArygonMessage(header, payload);
			}
			catch (Exception e) {
			}
		}

		throw new IOException("unparsable message received");

	}

	public boolean hasData() throws IOException {
		return in.available() > 0;
	}
}

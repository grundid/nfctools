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

import java.io.IOException;

import org.nfctools.NfcTimeoutException;
import org.nfctools.io.ByteArrayReader;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reader for the TAMA message format. This reader reads messages in the TAMA format and returns only the payload of a
 * message (packet data) including the 0xD5 response byte. All ACK frames are silently ignored, but the first ACK frame
 * must arrive within a timeout of 2 seconds. Any ERROR frames will be thrown as TamaExceptions containing the error
 * code.
 * 
 * If no data is available from the underlying ByteArrayReader this reader sleeps for 1ms.
 * 
 * TODO check the checksum and throw a checksum exception
 * 
 * TODO make the timeout configurable
 * 
 * @see TamaException
 * 
 */
public class TamaReader implements ByteArrayReader {

	protected Logger log = LoggerFactory.getLogger(getClass());
	private final ByteArrayReader reader;

	private byte[] buffer = new byte[1024];
	private int bufPos = 0;
	private boolean useDataFrameTimeout = false;
	private long dataFrameTimeout = 0;

	public TamaReader(ByteArrayReader reader) {
		this.reader = reader;
	}

	@Override
	public void setTimeout(long millis) {
		dataFrameTimeout = millis;
		useDataFrameTimeout = millis >= 0;
		reader.setTimeout(millis);
	}

	@Override
	public int read(byte[] data, int offset, int length) throws IOException {
		byte[] response = readResponse();
		if (response.length > length - offset)
			throw new IllegalArgumentException("buffer too small for response, needed " + response.length + " bytes");

		System.arraycopy(response, 0, data, offset, response.length);
		return response.length;
	}

	public byte[] readResponse() throws IOException {
		long timeoutTillFirstAckFrame = 2000;
		boolean useAckFrameTimeout = true;
		long timeoutCounter = System.currentTimeMillis();
		long timeoutCounterForDataFrame = System.currentTimeMillis();
		while (true) {
			if (log.isTraceEnabled())
				log.trace("reading... @" + bufPos);
			int read = reader.read(buffer, bufPos, buffer.length - bufPos);
			if (read <= 0) {
				try {
					Thread.sleep(1);
				}
				catch (InterruptedException e) {
				}
			}
			if (read >= 0) {
				bufPos += read;
				if (log.isTraceEnabled())
					log.trace("data read: " + read + "  " + NfcUtils.convertBinToASCII(buffer, 0, bufPos) + "/"
							+ bufPos);

				if (bufPos >= 6) // we need at least 6 bytes to identify a message
				{
					for (int x = 0; x < bufPos; x++) {
						if (x + 6 <= bufPos) // Check for ACK frame
						{
							if (TamaUtils.isACKFrame(buffer, x)) {
								byte[] resp = new byte[6];
								System.arraycopy(buffer, x, resp, 0, resp.length); // Copy data into response array
								if (log.isDebugEnabled())
									log.debug("Ack frame:" + NfcUtils.convertBinToASCII(resp));
								removeFrameFromBuffer(x, resp);

								useAckFrameTimeout = false;
								timeoutCounterForDataFrame = System.currentTimeMillis();
								// Ignore ACK Frames
							}
						}

						if (x + 8 <= bufPos) // Check for ERROR frame
						{
							if (TamaUtils.isErrorFrame(buffer, x)) {
								byte[] resp = new byte[8];
								System.arraycopy(buffer, x, resp, 0, resp.length); // Copy data into response array
								removeFrameFromBuffer(x, resp);
								int status = TamaUtils.getErrorCodeFromStatus(resp[5]);
								throw new TamaException(status);
							}
						}

						if (x + 5 <= bufPos) // Check for DATA frame
						{
							int msgLength = (buffer[x + 3] & 0xFF) + 7;

							if ((buffer[x] == 0x00) && (buffer[x + 1] == 0x00) && (buffer[x + 2] == (byte)0xFF)
									&& (msgLength + x <= bufPos) && buffer[x + 3] == (byte)-buffer[x + 4]) {
								byte[] resp = new byte[msgLength];
								System.arraycopy(buffer, x, resp, 0, msgLength);
								if (log.isDebugEnabled())
									log.debug("Data frame:" + NfcUtils.convertBinToASCII(resp));
								removeFrameFromBuffer(x, resp);
								return TamaUtils.unpackPayload(resp);
							}
						}
					}
				}
			}
			if (useDataFrameTimeout && System.currentTimeMillis() - timeoutCounterForDataFrame > dataFrameTimeout) {
				resetBuffer();
				throw new NfcTimeoutException();
			}

			if ((useAckFrameTimeout) && (System.currentTimeMillis() - timeoutCounter > timeoutTillFirstAckFrame)) {
				resetBuffer();
				throw new NfcTimeoutException("No complete message within timeout. Msg: ["
						+ NfcUtils.convertBinToASCII(buffer, 0, bufPos) + "] Length: " + bufPos);
			}
		}
	}

	private void resetBuffer() {
		bufPos = 0;

	}

	private void removeFrameFromBuffer(int x, byte[] resp) {
		System.arraycopy(buffer, x + resp.length, buffer, 0, bufPos - (resp.length + x)); // Move data in the buffer
		bufPos -= (resp.length + x);
	}
}

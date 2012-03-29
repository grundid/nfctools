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

import org.nfctools.io.ByteArrayWriter;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writer for the TAMA message format. This writer adds a Preamble of one byte (0x00), the start code (0x00FF), the
 * length and the checksum for the message and a Postamble of one byte (0x00). The writer also checks if the payload is
 * within the maximum message length of 252 bytes.
 * 
 */
public class TamaWriter implements ByteArrayWriter {

	protected Logger log = LoggerFactory.getLogger(getClass());
	private ByteArrayWriter writer;

	public TamaWriter(ByteArrayWriter writer) {
		this.writer = writer;
	}

	@Override
	public void write(byte[] data, int offset, int length) throws IOException {

		if (data.length > 252)
			throw new IllegalArgumentException("Message too long. Max 252 bytes. (was: " + data.length + ")");

		byte[] dataToWrite = new byte[7 + data.length];
		dataToWrite[0] = 0x00; // Preamble
		dataToWrite[1] = 0x00; // Startcode
		dataToWrite[2] = (byte)0xFF; // Startcode
		dataToWrite[3] = (byte)data.length; // Length
		dataToWrite[4] = (byte)-dataToWrite[3]; // Length Checksum
		System.arraycopy(data, 0, dataToWrite, 5, data.length); // Payload

		byte dcs = 0;
		for (int x = 0; x < data.length; x++) {
			dcs += data[x];
		}
		dataToWrite[5 + data.length] = (byte)-dcs; // Checksum
		dataToWrite[6 + data.length] = 0x00; // Postamble

		if (log.isDebugEnabled())
			log.debug("Sending frame:  " + NfcUtils.convertBinToASCII(dataToWrite));

		writer.write(dataToWrite, 0, dataToWrite.length);
	}

}

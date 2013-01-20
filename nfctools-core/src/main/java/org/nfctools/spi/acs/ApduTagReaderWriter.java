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
package org.nfctools.spi.acs;

import java.io.IOException;

import org.nfctools.api.ApduTag;
import org.nfctools.io.ByteArrayReader;
import org.nfctools.io.ByteArrayWriter;
import org.nfctools.scio.Command;
import org.nfctools.scio.Response;
import org.nfctools.utils.NfcUtils;

public class ApduTagReaderWriter implements ByteArrayReader, ByteArrayWriter {

	private byte[] responseData;
	private final ApduTag apduTag;

	public ApduTagReaderWriter(ApduTag apduTag) {
		this.apduTag = apduTag;
	}

	@Override
	public void write(byte[] data, int offset, int length) throws IOException {
		Command command = new Command(data, offset, length);
		Response response = apduTag.transmit(command);
		responseData = response.getData();
		if (!response.isSuccess())
			throw new ApduException("Error sending message [" + NfcUtils.convertBinToASCII(data) + "] (" + offset + ","
					+ length + ") => [SW1: " + response.getSw1() + ", SW2:" + response.getSw2() + ", Data: "
					+ NfcUtils.convertBinToASCII(responseData) + "]");
	}

	@Override
	public void setTimeout(long millis) {
	}

	@Override
	public int read(byte[] data, int offset, int length) throws IOException {
		if (responseData.length > length - offset)
			throw new IllegalArgumentException("buffer too small for response, needed " + responseData.length
					+ " bytes");
		System.arraycopy(responseData, 0, data, offset, responseData.length);
		return responseData.length;
	}
}

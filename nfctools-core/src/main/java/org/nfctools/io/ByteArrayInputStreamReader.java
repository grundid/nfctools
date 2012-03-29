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
package org.nfctools.io;

import java.io.IOException;
import java.io.InputStream;

import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple reader which directly reads data from an InputStream without any modification.
 * 
 */
public class ByteArrayInputStreamReader implements ByteArrayReader {

	private Logger log = LoggerFactory.getLogger(getClass());
	private InputStream inputStream;

	public ByteArrayInputStreamReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public void setTimeout(long millis) {
	}

	@Override
	public int read(byte[] data, int offset, int length) throws IOException {
		int dataRead = inputStream.read(data, offset, length);

		if (log.isDebugEnabled() && dataRead > 0)
			log.debug("[" + NfcUtils.convertBinToASCII(data, offset, dataRead) + "]");

		return dataRead;
	}
}

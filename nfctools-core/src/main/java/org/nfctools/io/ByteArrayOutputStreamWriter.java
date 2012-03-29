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
import java.io.OutputStream;

import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple writer which directly writes data into an OutputStream without modification.
 * 
 */
public class ByteArrayOutputStreamWriter implements ByteArrayWriter {

	private Logger log = LoggerFactory.getLogger(getClass());

	private OutputStream outputStream;

	public ByteArrayOutputStreamWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void write(byte[] data, int offset, int length) throws IOException {
		if (log.isDebugEnabled())
			log.debug(NfcUtils.convertBinToASCII(data, offset, length));
		outputStream.write(data, offset, length);
	}
}

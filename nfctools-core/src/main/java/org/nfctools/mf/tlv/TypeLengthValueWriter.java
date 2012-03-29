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


package org.nfctools.mf.tlv;

import java.io.IOException;
import java.io.OutputStream;

public class TypeLengthValueWriter {

	private OutputStream out;

	public TypeLengthValueWriter(OutputStream out) {
		this.out = out;
	}

	public void write(byte[] data) throws IOException {
		if (data == null || data.length == 0) {
			out.write(TlvConstants.NULL_TLV);
		}
		else {
			out.write(TlvConstants.NDEF_TLV);
			if (data.length <= 0xFE) {
				out.write(data.length);
			}
			else if (data.length < 0xFFFE) {
				out.write(0xFF);
				out.write(data.length >>> 8);
				out.write(data.length & 0xFF);
			}
			else {
				throw new IOException("data too long");
			}

			out.write(data, 0, data.length);
		}
	}

	public void close() throws IOException {
		out.write(TlvConstants.TERMINATOR_TLV);
	}
}

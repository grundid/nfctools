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
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TypeLengthValueReader implements Iterator<byte[]> {

	private InputStream in = null;
	private byte[] nextValue = null;

	public TypeLengthValueReader(InputStream in) {
		this.in = in;
	}

	public byte[] next() {
		if (nextValue != null) {
			byte[] valueToReturn = nextValue;
			nextValue = null;
			return valueToReturn;
		}
		else {
			try {
				while (in.available() > 0) {
					int type = in.read();

					while (type == TlvConstants.NULL_TLV && type != -1) {
						type = in.read();
					}

					if (type == TlvConstants.TERMINATOR_TLV)
						throw new NoSuchElementException();

					if (type == TlvConstants.NDEF_TLV) {
						return readNextValue();
					}
					if (type == TlvConstants.PROPRIETARY_TLV) {
						readNextValue();
					}
				}
				throw new NoSuchElementException();
			}
			catch (IOException e) {
				throw new NoSuchElementException(e.getMessage());
			}
		}
	}

	private byte[] readNextValue() throws IOException {
		int size = in.read();
		if (size == 0xFF) {
			size = (in.read() << 8) | in.read();
		}
		byte[] valueToReturn = new byte[size];
		in.read(valueToReturn, 0, size);
		return valueToReturn;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public boolean hasNext() {
		if (nextValue != null)
			return true;
		else {
			try {
				nextValue = next();
				return true;
			}
			catch (NoSuchElementException e) {
				return false;
			}
		}
	}

}

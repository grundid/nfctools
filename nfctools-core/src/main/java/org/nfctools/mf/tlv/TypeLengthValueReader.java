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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TypeLengthValueReader implements Iterator<Tlv> {

	public final static void readFully(InputStream in, byte b[], int off, int len) throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = in.read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}
	
	private InputStream in = null;
	private Tlv nextValue = null;

	public TypeLengthValueReader(InputStream in) {
		this.in = in;
	}

	@Override
	public Tlv next() {
		if (nextValue != null) {
			Tlv valueToReturn = nextValue;
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

					if (TlvConstants.KNOWN_TLVS.contains(type)) {
						return readNextValue(type);
					}
					if (type == TlvConstants.PROPRIETARY_TLV) {
						readNextValue(type); // read and ignore
					}
				}
				throw new NoSuchElementException();
			}
			catch (IOException e) {
				throw new NoSuchElementException(e.getMessage());
			}
		}
	}

	private Tlv readNextValue(int type) throws IOException {
		
		int size = in.read();
		
		if (size == -1) {
			throw new EOFException();
		}

		if (size == 0xFF) {
			int ch1 = in.read();
			int ch2 = in.read();
			if ((ch1 == -1 || ch2 == -1)) {
				throw new EOFException();
			}
			
			size = ((ch1 << 8) + (ch2 << 0));
		}
		
		byte[] bytes = new byte[size];
		
		readFully(in, bytes, 0, bytes.length);

		switch (type) {
			case TlvConstants.NDEF_TLV:
				return new NdefMessageTlv(bytes);
			case TlvConstants.LOCK_CONTROL_TLV:
				return new LockControlTlv(bytes);
			case TlvConstants.MEMORY_CONTROL_TLV:
				return new MemoryControlTlv(bytes);
			case TlvConstants.PROPRIETARY_TLV:
				return null; // skip it
		}

		throw new RuntimeException("unkown TLV type " + type + "");
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
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

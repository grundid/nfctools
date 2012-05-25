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
package org.nfctools.snep;

import java.util.Iterator;

public class FragmentIterator implements Iterator<byte[]> {

	private byte[] data;
	private int maxFragmentSize;
	private int offset = 0;

	public FragmentIterator(byte[] data, int maxFragmentSize) {
		this.data = data;
		this.maxFragmentSize = maxFragmentSize;
	}

	@Override
	public boolean hasNext() {
		return offset < data.length;
	}

	@Override
	public byte[] next() {
		byte[] buffer = new byte[Math.min(data.length - offset, maxFragmentSize)];
		System.arraycopy(data, offset, buffer, 0, buffer.length);
		offset += buffer.length;
		return buffer;
	}

	@Override
	public void remove() {
	}
}

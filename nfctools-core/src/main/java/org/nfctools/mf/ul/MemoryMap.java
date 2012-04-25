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
package org.nfctools.mf.ul;

import java.util.Arrays;

import org.nfctools.mf.tlv.LockControlTlv;

public class MemoryMap {

	private byte[][] memory;
	private LockControlTlv dynamicLock;

	public MemoryMap(int pages) {
		memory = new byte[pages][4];
	}

	public void setPage(int pageNo, byte[] bytes) {
		setPage(pageNo, bytes, 0);
	}

	public void setPage(int pageNo, byte[] bytes, int offset) {
		for (int x = 0; x < 4; x++)
			memory[pageNo][x] = bytes[offset + x];
	}

	public byte[] getPage(int pageNo) {
		return memory[pageNo];
	}

	public byte[][] getMemory() {
		return memory;
	}

	public LockControlTlv getDynamicLock() {
		return dynamicLock;
	}

	public void setDynamicLock(LockControlTlv dynamicLock) {
		this.dynamicLock = dynamicLock;
	}

	public boolean hasDynamicLock() {
		return dynamicLock != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(memory);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemoryMap other = (MemoryMap)obj;
		if (!Arrays.equals(memory, other.memory))
			return false;
		return true;
	}

}

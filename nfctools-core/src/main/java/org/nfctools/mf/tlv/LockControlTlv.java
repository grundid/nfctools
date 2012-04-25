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

import org.nfctools.utils.NfcUtils;

public class LockControlTlv extends AbstractMemoryTlv {

	private int bytesLockedPerLockBit;

	public LockControlTlv() {
	}

	public LockControlTlv(byte[] bytes) {
		super(bytes);
		bytesLockedPerLockBit = NfcUtils.getMostSignificantNibble(bytes[2]);
	}

	@Override
	public byte[] toBytes() {
		byte[] bytes = super.toBytes();
		bytes[2] = NfcUtils.encodeNibbles(bytesLockedPerLockBit, bytesPerPage);
		return bytes;
	}

	public int getBytesLockedPerLockBit() {
		return bytesLockedPerLockBit;
	}

	public void setBytesLockedPerLockBit(int bytesLockedPerLockBit) {
		this.bytesLockedPerLockBit = bytesLockedPerLockBit;
	}

	public int getSizeInBytes() {
		return (int)Math.ceil(getSize() / 8);
	}

	/**
	 * It indicates the size in bits of the lock area i.e. the number of dynamic lock bits.
	 */
	@Override
	public int getSize() {
		return super.getSize();
	}
}

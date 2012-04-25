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

import static org.junit.Assert.*;

import org.junit.Test;

public class LockControlTlvTest {

	private byte[] lockControlData = { (byte)0xA0, 0x10, 0x44 };

	@Test
	public void testLockControlTlv() throws Exception {
		LockControlTlv lockControlTlv = new LockControlTlv(lockControlData);

		assertEquals(0x0a, lockControlTlv.getPageAddress());
		assertEquals(0x00, lockControlTlv.getByteOffset());
		assertEquals(0x10, lockControlTlv.getSize());
		assertEquals(0x02, lockControlTlv.getSizeInBytes());
		assertEquals(0x04, lockControlTlv.getBytesPerPage());
		assertEquals(0x04, lockControlTlv.getBytesLockedPerLockBit());

		assertEquals(160, lockControlTlv.getPosition());

		assertArrayEquals(lockControlData, lockControlTlv.toBytes());

	}
}

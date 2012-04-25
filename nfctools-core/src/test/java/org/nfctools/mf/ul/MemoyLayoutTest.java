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

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.mf.tlv.LockControlTlv;

public class MemoyLayoutTest {

	@Test
	public void testLockControlTlv() throws Exception {
		LockControlTlv lockControlTlv = MemoryLayout.ULTRALIGHT_C.createLockControlTlv();
		assertArrayEquals(new byte[] { (byte)0xA0, 0x10, 0x44 }, lockControlTlv.toBytes());
	}

	@Test
	public void testCreateCapabilityBlockUltralightC() throws Exception {
		CapabilityBlock capabilityBlock = MemoryLayout.ULTRALIGHT_C.createCapabilityBlock();

		assertEquals(0x12, capabilityBlock.getSize());
		assertEquals(0x10, capabilityBlock.getVersion());
		assertFalse(capabilityBlock.isReadOnly());
	}

	@Test
	public void testCreateCapabilityBlockUltralight() throws Exception {
		CapabilityBlock capabilityBlock = MemoryLayout.ULTRALIGHT.createCapabilityBlock();

		assertEquals(0x06, capabilityBlock.getSize());
		assertEquals(0x10, capabilityBlock.getVersion());
		assertFalse(capabilityBlock.isReadOnly());
	}
}

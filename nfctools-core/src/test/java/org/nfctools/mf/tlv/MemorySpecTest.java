package org.nfctools.mf.tlv;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemorySpecTest {

	private byte[] lockControlData = { (byte)0xA0, 0x10, 0x44 };

	@Test
	public void testMemorySpec() throws Exception {
		LockControlTlv memorySpec = new LockControlTlv(lockControlData);

		assertEquals(0x0a, memorySpec.getPageAddress());
		assertEquals(0x00, memorySpec.getByteOffset());
		assertEquals(0x10, memorySpec.getSize());
		assertEquals(0x02, memorySpec.getSizeInBytes());
		assertEquals(0x04, memorySpec.getBytesPerPage());
		assertEquals(0x04, memorySpec.getBytesLockedPerLockBit());

		assertEquals(160, memorySpec.getPosition());

		assertArrayEquals(lockControlData, memorySpec.toBytes());

	}
}

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

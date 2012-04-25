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

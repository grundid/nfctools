package org.nfctools.mf;

import static org.junit.Assert.*;

import org.junit.Test;

public class MfUtilsTest {

	@Test
	public void testNibbleEncoding() throws Exception {
		assertEquals(0x0A, MfUtils.getMostSignificantNibble((byte)0xAE));
		assertEquals(0x0E, MfUtils.getLeastSignificantNibble((byte)0xAE));
		assertEquals((byte)0xAE, MfUtils.encodeNibbles(0x0A, 0x0E));
	}
}

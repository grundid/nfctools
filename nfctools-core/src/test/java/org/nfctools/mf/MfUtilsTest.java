package org.nfctools.mf;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.utils.NfcUtils;

public class MfUtilsTest {

	@Test
	public void testNibbleEncoding() throws Exception {
		assertEquals(0x0A, NfcUtils.getMostSignificantNibble((byte)0xAE));
		assertEquals(0x0E, NfcUtils.getLeastSignificantNibble((byte)0xAE));
		assertEquals((byte)0xAE, NfcUtils.encodeNibbles(0x0A, 0x0E));
	}
}

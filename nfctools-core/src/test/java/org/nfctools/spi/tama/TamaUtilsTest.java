package org.nfctools.spi.tama;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.utils.NfcUtils;

public class TamaUtilsTest {

	@Test
	public void testUnpackPayload() throws Exception {

		byte[] payload = TamaUtils.unpackPayload(NfcUtils.convertASCIIToBin("0000FF02FED5131800"));

		assertArrayEquals(new byte[] { (byte)0xd5, 0x13 }, payload);

	}
}

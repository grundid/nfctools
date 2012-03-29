package org.nfctools.ndefpush;

import static org.junit.Assert.*;

import org.junit.Test;

public class LlcpTargetListenerTest {

	@Test
	public void testExtractLlcParameters() throws Exception {

		byte[] generalBytes = { 0x1E, (byte)0xD4, 0x00, 0x01, (byte)0xFE, 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x32, 0x46, 0x66, 0x6D, 0x01, 0x01, 0x10, 0x03, 0x02, 0x00, 0x01, 0x04, 0x01,
				(byte)0x96 };

		LlcpTargetListener llcpTargetListener = new LlcpTargetListener(null);

		Object[] llcParameters = llcpTargetListener.extractLlcParameters(generalBytes);

		assertEquals(3, llcParameters.length);

	}
}

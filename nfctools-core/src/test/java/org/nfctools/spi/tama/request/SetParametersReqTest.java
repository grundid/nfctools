package org.nfctools.spi.tama.request;

import static org.junit.Assert.*;

import org.junit.Test;

public class SetParametersReqTest {

	@Test
	public void testSetParameters() throws Exception {
		SetParametersReq req = new SetParametersReq();
		assertEquals(0x14, req.getFlags());
		req.setAutomaticATR_RES(false);
		assertEquals(0x10, req.getFlags());
	}
}

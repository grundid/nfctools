package org.nfctools.llcp;

import static org.junit.Assert.*;

import org.junit.Test;

public class LlcpSocketTest {

	@Test
	public void testSequences() throws Exception {
		LlcpSocket llcpSocket = new LlcpSocket(new AddressPair(0, 0), null);
		assertEquals(0, llcpSocket.getSendSequence());
		llcpSocket.incSendSequence();
		assertEquals(1, llcpSocket.getSendSequence());

		assertEquals(0, llcpSocket.getReceivedSequence());
		llcpSocket.incReceivedSequence();
		assertEquals(1, llcpSocket.getReceivedSequence());
	}
}

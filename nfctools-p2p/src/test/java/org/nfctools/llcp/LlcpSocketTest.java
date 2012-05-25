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

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 16; x++) {
				assertEquals(x, llcpSocket.getReceivedSequence());
				llcpSocket.incReceivedSequence();
			}
		}
	}
}

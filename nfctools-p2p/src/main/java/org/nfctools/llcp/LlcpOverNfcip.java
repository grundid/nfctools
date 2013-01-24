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

import java.io.IOException;

import org.nfctools.llcp.pdu.AbstractProtocolDataUnit;
import org.nfctools.llcp.pdu.PduDecoder;
import org.nfctools.llcp.pdu.Symmetry;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPConnectionListener;

public class LlcpOverNfcip implements NFCIPConnectionListener {

	private PduDecoder pduDecoder = new PduDecoder();
	private LlcpConnectionManagerFactory connectionManagerFactory;

	public LlcpOverNfcip(LlcpConnectionManagerFactory connectionManagerFactory) {
		this.connectionManagerFactory = connectionManagerFactory;
	}

	@Override
	public void onConnection(NFCIPConnection connection) throws IOException {
		LlcpConnectionManager connectionManager = connectionManagerFactory.createInstance();
		connectionManager.init(extractParameters(connection.getGeneralBytes()));
		try {
			byte[] data = connection.isInitiator() ? pduDecoder.encode(new Symmetry()) : connection.receive();
			while (!Thread.interrupted()) {
				AbstractProtocolDataUnit requestPdu = pduDecoder.decode(data);
				AbstractProtocolDataUnit responsePdu = requestPdu.processPdu(connectionManager);
				byte[] pdu = pduDecoder.encode(responsePdu);
				connection.send(pdu);
				data = connection.receive();
			}
		}
		finally {
			connectionManager.clearConnections();
		}
	}

	public Object[] extractParameters(byte[] generalBytes) {
		if (generalBytes.length >= 3) {
			if (generalBytes[0] == 0x46 && generalBytes[1] == 0x66 && generalBytes[2] == 0x6D) {
				return pduDecoder.decodeParameter(generalBytes, 3);
			}
		}
		return new Object[0];
	}
}

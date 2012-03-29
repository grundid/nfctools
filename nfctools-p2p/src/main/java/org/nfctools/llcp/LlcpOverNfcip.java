package org.nfctools.llcp;

import java.io.IOException;

import org.nfctools.llcp.pdu.AbstractProtocolDataUnit;
import org.nfctools.llcp.pdu.PduDecoder;
import org.nfctools.llcp.pdu.Symmetry;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPConnectionListener;

public class LlcpOverNfcip implements NFCIPConnectionListener {

	private PduDecoder pduDecoder = new PduDecoder();
	private LlcpConnectionManager connectionManager = new LlcpConnectionManager();

	@Override
	public void onConnection(NFCIPConnection connection) throws IOException {
		initFromGeneralBytes(connection.getTarget().getGeneralBytes());
		try {
			if (connection.isInitiator()) {
				connection.send(pduDecoder.encode(new Symmetry().processPdu(connectionManager)));
			}

			while (!Thread.interrupted()) {
				byte[] data = connection.receive();
				AbstractProtocolDataUnit requestPdu = pduDecoder.decode(data);

				AbstractProtocolDataUnit responsePdu = requestPdu.processPdu(connectionManager);
				byte[] pdu = pduDecoder.encode(responsePdu);
				connection.send(pdu);
			}
		}
		finally {
			clearStack();
		}
	}

	private void initFromGeneralBytes(byte[] generalBytes) {
		if (generalBytes.length >= 3) {
			if (generalBytes[0] == 0x46 && generalBytes[1] == 0x66 && generalBytes[2] == 0x6D) {
				Object[] parameters = pduDecoder.decodeParameter(generalBytes, 3);
				connectionManager.init(parameters);
			}
		}
	}

	public LlcpConnectionManager getConnectionManager() {
		return connectionManager;
	}

	private void clearStack() {
		connectionManager.clearConnections();
	}
}

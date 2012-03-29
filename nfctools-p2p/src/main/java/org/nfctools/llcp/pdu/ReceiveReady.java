package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class ReceiveReady extends AbstractSequenceProtocolDataUnit {

	public ReceiveReady(int destinationServiceAccessPoint, int sourceServiceAccessPoint, int received) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint, received, 0);
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		return connectionManager.onSendConfirmed(getSourceServiceAccessPoint(), getDestinationServiceAccessPoint(),
				getReceived());
	}
}

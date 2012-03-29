package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class Disconnect extends AbstractProtocolDataUnit {

	public Disconnect(int destinationServiceAccessPoint, int sourceServiceAccessPoint) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint);
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		return connectionManager.onDisconnect(getSourceServiceAccessPoint(), getDestinationServiceAccessPoint());
	}

}

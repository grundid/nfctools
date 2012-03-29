package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class ConnectComplete extends AbstractParameterProtocolDataUnit {

	public ConnectComplete(int destinationServiceAccessPoint, int sourceServiceAccessPoint, Object... parameter) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint, parameter);
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		return connectionManager.onConnectComplete(getSourceServiceAccessPoint(), getDestinationServiceAccessPoint(),
				getParameter());
	}
}

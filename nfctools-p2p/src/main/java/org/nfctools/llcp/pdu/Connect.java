package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class Connect extends AbstractParameterProtocolDataUnit {

	public Connect(int destinationServiceAccessPoint, int sourceServiceAccessPoint, Object... parameter) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint, parameter);
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		return connectionManager.onConnect(getSourceServiceAccessPoint(), getDestinationServiceAccessPoint(),
				getParameter());
	}
}

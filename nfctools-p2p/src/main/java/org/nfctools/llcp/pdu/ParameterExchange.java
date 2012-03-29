package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class ParameterExchange extends AbstractParameterProtocolDataUnit {

	public ParameterExchange(int destinationServiceAccessPoint, int sourceServiceAccessPoint, Object... parameter) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint, parameter);
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		throw new IllegalStateException();
	}

}

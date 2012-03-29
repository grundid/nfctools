package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class DisconnectedMode extends AbstractProtocolDataUnit {

	private int reason;

	public DisconnectedMode(int destinationServiceAccessPoint, int sourceServiceAccessPoint, int reason) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint);
		this.reason = reason;
	}

	public int getReason() {
		return reason;
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		return connectionManager.onDisconnectedMode(getSourceServiceAccessPoint(), getDestinationServiceAccessPoint(),
				reason);
	}

}

package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public abstract class AbstractProtocolDataUnit {

	private int destinationServiceAccessPoint;
	private int sourceServiceAccessPoint;

	protected AbstractProtocolDataUnit(int destinationServiceAccessPoint, int sourceServiceAccessPoint) {
		this.destinationServiceAccessPoint = destinationServiceAccessPoint;
		this.sourceServiceAccessPoint = sourceServiceAccessPoint;
	}

	public int getDestinationServiceAccessPoint() {
		return destinationServiceAccessPoint;
	}

	public int getSourceServiceAccessPoint() {
		return sourceServiceAccessPoint;
	}

	@Override
	public String toString() {
		return super.toString() + " Dest: " + destinationServiceAccessPoint + "|Src: " + sourceServiceAccessPoint;
	}

	public abstract AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager);
}

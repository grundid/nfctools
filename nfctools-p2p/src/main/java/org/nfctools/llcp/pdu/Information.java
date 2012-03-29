package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class Information extends AbstractSequenceProtocolDataUnit {

	private byte[] serviceDataUnit;

	public Information(int destinationServiceAccessPoint, int sourceServiceAccessPoint, int received, int send,
			byte[] serviceDataUnit) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint, received, send);
		this.serviceDataUnit = serviceDataUnit;
	}

	public byte[] getServiceDataUnit() {
		return serviceDataUnit;
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		return connectionManager.onReceiveInformation(getSourceServiceAccessPoint(),
				getDestinationServiceAccessPoint(), getReceived(), getSend(), serviceDataUnit);
	}
}

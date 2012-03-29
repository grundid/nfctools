package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class UnnumberedInformation extends AbstractProtocolDataUnit {

	private byte[] serviceDataUnit;

	public UnnumberedInformation(int destinationServiceAccessPoint, int sourceServiceAccessPoint, byte[] serviceDataUnit) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint);
		this.serviceDataUnit = serviceDataUnit;
	}

	public byte[] getServiceDataUnit() {
		return serviceDataUnit;
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		throw new IllegalStateException();
	}
}

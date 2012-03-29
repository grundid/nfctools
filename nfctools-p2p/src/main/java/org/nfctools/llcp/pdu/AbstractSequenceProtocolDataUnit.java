package org.nfctools.llcp.pdu;

public abstract class AbstractSequenceProtocolDataUnit extends AbstractProtocolDataUnit {

	private int received;
	private int send;

	protected AbstractSequenceProtocolDataUnit(int destinationServiceAccessPoint, int sourceServiceAccessPoint,
			int received, int send) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint);
		this.received = received;
		this.send = send;
	}

	public int getReceived() {
		return received;
	}

	public int getSend() {
		return send;
	}

}

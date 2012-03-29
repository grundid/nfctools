package org.nfctools.llcp;

import org.nfctools.llcp.pdu.AbstractProtocolDataUnit;
import org.nfctools.llcp.pdu.Disconnect;
import org.nfctools.llcp.pdu.DisconnectedMode;
import org.nfctools.llcp.pdu.Information;
import org.nfctools.llcp.pdu.ReceiveReady;
import org.nfctools.llcp.pdu.Symmetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LlcpSocket {

	private Logger log = LoggerFactory.getLogger(getClass());

	private AddressPair addressPair;

	private int receivedSequence;
	private int sendSequence;

	private int maximumInformationUnitExtension = 0;

	private AbstractProtocolDataUnit messageToSend;
	private ServiceAccessPoint serviceAccessPoint;

	public LlcpSocket(AddressPair addressPair, ServiceAccessPoint serviceAccessPoint) {
		this.addressPair = addressPair;
		this.serviceAccessPoint = serviceAccessPoint;
	}

	public void setMaximumInformationUnitExtension(int maximumInformationUnitExtension) {
		this.maximumInformationUnitExtension = maximumInformationUnitExtension;
	}

	public void incReceivedSequence() {
		receivedSequence = ++receivedSequence % 16;
	}

	public void incSendSequence() {
		sendSequence = ++sendSequence % 16;
	}

	public int getReceivedSequence() {
		return receivedSequence;
	}

	public int getSendSequence() {
		return sendSequence;
	}

	public void sendMessage(byte[] message) {
		if (message.length > getMaximumInformationUnit())
			throw new IllegalArgumentException("Message too long. Maximum Information Unit is "
					+ getMaximumInformationUnit());

		messageToSend = new Information(addressPair.getRemote(), addressPair.getLocal(), getReceivedSequence(),
				getSendSequence(), message);
		incSendSequence();
	}

	public void onSendConfirmed(int received) {
		if (sendSequence != received)
			log.warn("sequences do not match myS: " + sendSequence + " myR: " + receivedSequence + " hisR: " + received);

		serviceAccessPoint.onSendSucceeded(this);
	}

	public AbstractProtocolDataUnit getMessageToSend() {
		if (messageToSend == null)
			return new Symmetry();
		AbstractProtocolDataUnit pdu = messageToSend;
		messageToSend = null;
		return pdu;
	}

	public int getMaximumInformationUnit() {
		return maximumInformationUnitExtension + LlcpConstants.DEFAULT_MIU;
	}

	public void disconnect() {

		messageToSend = new Disconnect(addressPair.getRemote(), addressPair.getLocal());
	}

	public void onInformation(int received, int send, byte[] serviceDataUnit) {
		if (send != receivedSequence)
			log.warn("sequences do not match myS: " + sendSequence + " myR: " + receivedSequence + " hisS: " + send
					+ " Msg-Length: " + serviceDataUnit.length);

		// TODO can you send and receive messages at the same time? can an I PDU be used as a confirm frame?

		serviceAccessPoint.onInformation(serviceDataUnit);

		incReceivedSequence();
		messageToSend = new ReceiveReady(addressPair.getRemote(), addressPair.getLocal(), getReceivedSequence());
	}

	public void onConnectSucceeded() {
		serviceAccessPoint.onConnectSucceeded(this);
	}

	public void onDisconnect() {
		serviceAccessPoint.onDisconnect();
		messageToSend = new DisconnectedMode(addressPair.getRemote(), addressPair.getLocal(), 0);
	}

	public void onDisconnectSucceeded() {
		serviceAccessPoint.onDisconnect();

	}

	public void onConnectFailed(int reason) {
		serviceAccessPoint.onConnectFailed();
	}

	public boolean equalsAddress(AddressPair addressPair) {
		return this.addressPair.equals(addressPair);
	}
}

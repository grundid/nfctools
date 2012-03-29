package org.nfctools.llcp.pdu;

import org.nfctools.llcp.Llcp;
import org.nfctools.llcp.LlcpSocket;
import org.nfctools.llcp.ServiceAccessPoint;

public class DummyServiceAccessPoint implements ServiceAccessPoint {

	private String messageToSend;
	private String messageReceived;
	private String serviceToConnect;

	private boolean acceptConnections = true;
	private boolean connected = false;

	public DummyServiceAccessPoint() {
	}

	public DummyServiceAccessPoint(String serviceToConnect) {
		this.serviceToConnect = serviceToConnect;
	}

	public DummyServiceAccessPoint(String message, String serviceToConnect) {
		this.messageToSend = message;
		this.serviceToConnect = serviceToConnect;
	}

	@Override
	public void onConnectFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLlcpActive(Llcp llcp) {
		if (messageToSend != null) {
			llcp.connectToService(serviceToConnect, this);
		}
	}

	@Override
	public void onConnectSucceeded(LlcpSocket llcpSocket) {
		connected = true;
		llcpSocket.sendMessage(messageToSend.getBytes());
	}

	@Override
	public void onSendSucceeded(LlcpSocket llcpSocket) {
		if (messageToSend != null) {
			messageToSend = null;
			llcpSocket.disconnect();
		}

	}

	@Override
	public void onSendFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canAcceptConnection(Object[] parameters) {
		return acceptConnections;
	}

	@Override
	public void onInformation(byte[] serviceDataUnit) {
		messageReceived = new String(serviceDataUnit);
	}

	public String getMessageReceived() {
		return messageReceived;
	}

	@Override
	public void onDisconnect() {
		connected = false;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setAcceptConnections(boolean acceptConnections) {
		this.acceptConnections = acceptConnections;
	}
}

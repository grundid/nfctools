package org.nfctools.llcp;

public interface ServiceAccessPoint {

	void onLlcpActive(Llcp llcp);

	void onConnectFailed();

	void onConnectSucceeded(LlcpSocket llcpSocket);

	void onSendSucceeded(LlcpSocket llcpSocket);

	void onSendFailed();

	boolean canAcceptConnection(Object[] parameters);

	void onInformation(byte[] serviceDataUnit);

	void onDisconnect();
}

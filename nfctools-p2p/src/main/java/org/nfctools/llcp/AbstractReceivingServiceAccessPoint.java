package org.nfctools.llcp;

public abstract class AbstractReceivingServiceAccessPoint implements ServiceAccessPoint {

	@Override
	public void onLlcpActive(Llcp llcp) {
	}

	@Override
	public void onConnectFailed() {
	}

	@Override
	public void onConnectSucceeded(LlcpSocket llcpSocket) {
	}

	@Override
	public void onSendSucceeded(LlcpSocket llcpSocket) {
	}

	@Override
	public void onSendFailed() {
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public boolean canAcceptConnection(Object[] parameters) {
		return true;
	}
}

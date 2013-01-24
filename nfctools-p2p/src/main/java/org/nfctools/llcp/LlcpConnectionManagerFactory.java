package org.nfctools.llcp;

public class LlcpConnectionManagerFactory {

	public LlcpConnectionManager createInstance() {
		LlcpConnectionManager connectionManager = new LlcpConnectionManager();
		configureConnectionManager(connectionManager);
		return connectionManager;
	}

	protected void configureConnectionManager(LlcpConnectionManager connectionManager) {
	}
}

package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class Symmetry extends AbstractProtocolDataUnit {

	public Symmetry() {
		super(0, 0);
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		return connectionManager.onLlcpActive();
	}
}

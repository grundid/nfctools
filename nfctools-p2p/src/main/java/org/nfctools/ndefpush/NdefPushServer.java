package org.nfctools.ndefpush;

import java.io.IOException;

import org.nfctools.llcp.LlcpConnectionListener;
import org.nfctools.nfcip.NFCIPManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NdefPushServer {

	private Logger log = LoggerFactory.getLogger(getClass());
	private NFCIPManager nfcipManager;
	private LlcpConnectionListener llcpConnectionListener = new LlcpConnectionListener();

	public NdefPushServer(NFCIPManager nfcipManager) {
		this.nfcipManager = nfcipManager;
	}

	public void startServer() throws IOException {
		nfcipManager.setTargetListener(new LlcpTargetListener(new byte[] { 0x01, 0x01, 0x10 }));
		nfcipManager.setConnectionListener(llcpConnectionListener);
		nfcipManager.initAsTarget();
		log.info("Server running...");
	}
}

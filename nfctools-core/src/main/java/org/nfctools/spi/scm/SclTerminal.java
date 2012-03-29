package org.nfctools.spi.scm;

import java.io.IOException;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;

import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.scio.AbstractTerminal;
import org.nfctools.scio.TerminalStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class SclTerminal extends AbstractTerminal {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Scl3711 scl3711;

	@Override
	public boolean canHandle(String terminalName) {
		return terminalName.contains("SCL3711");
	}

	@Override
	public void initInitiatorDep() throws IOException {
		while (!Thread.interrupted()) {
			try {
				Card card = cardTerminal.connect("direct");
				scl3711 = new Scl3711(card);
				notifyStatus(TerminalStatus.WAITING);
				log.info("Waiting...");
				try {
					Scl3711NfcipManager nfcipManager = new Scl3711NfcipManager(scl3711);
					NFCIPConnection nfcipConnection = nfcipManager.connectAsInitiator();
					handleNfcipConnection(nfcipConnection);
				}
				catch (Exception e) {
				}
				finally {
					log.info("Disconnect from card");
					card.disconnect(true);
					notifyStatus(TerminalStatus.DISCONNECTED);
				}
			}
			catch (CardException e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	public void initTargetDep() throws IOException {
		log.warn("Target mode not supported yet. Using initiator...");
		initInitiatorDep();
	}

	@Override
	protected void handleNfcipConnection(NFCIPConnection nfcipConnection) throws IOException {
		if (nfcipConnection != null && nfcipConnectionListener != null) {
			notifyStatus(TerminalStatus.CONNECTED);
			nfcipConnectionListener.onConnection(nfcipConnection);
		}
	}
}

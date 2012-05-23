package org.nfctools.spi.acs;

import java.io.IOException;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import org.nfctools.api.TagListener;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPConnectionListener;
import org.nfctools.scio.TerminalStatus;
import org.nfctools.scio.TerminalStatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTerminalTagScanner implements Runnable {

	protected Logger log = LoggerFactory.getLogger(getClass());
	protected CardTerminal cardTerminal;
	protected TerminalStatusListener statusListener;
	protected TagListener tagListener;
	protected NFCIPConnectionListener nfcipConnectionListener;

	protected AbstractTerminalTagScanner(CardTerminal cardTerminal) {
		this.cardTerminal = cardTerminal;
	}

	protected void notifyStatus(TerminalStatus status) {
		if (statusListener != null)
			statusListener.onStatusChanged(status);
	}

	protected void waitForCardAbsent() throws CardException {
		try {
			while (cardTerminal.isCardPresent()) {
				try {
					log.debug("Waiting while card present");
					Thread.sleep(500);
				}
				catch (InterruptedException e) {
					break;
				}
			}
			log.debug("Wait for card absent");
			cardTerminal.waitForCardAbsent(1000);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("Disconnected");
		notifyStatus(TerminalStatus.DISCONNECTED);
	}

	public void setNfcipConnectionListener(NFCIPConnectionListener nfcipConnectionListener) {
		this.nfcipConnectionListener = nfcipConnectionListener;
	}

	public void setTagListener(TagListener tagListener) {
		this.tagListener = tagListener;
	}

	public void setStatusListener(TerminalStatusListener statusListener) {
		this.statusListener = statusListener;
	}

	protected void handleNfcipConnection(NFCIPConnection nfcipConnection) throws IOException {
		if (nfcipConnection != null && nfcipConnectionListener != null) {
			nfcipConnectionListener.onConnection(nfcipConnection);
		}
	}

}

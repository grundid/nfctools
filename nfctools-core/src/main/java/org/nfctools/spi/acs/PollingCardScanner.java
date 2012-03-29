package org.nfctools.spi.acs;

import java.io.IOException;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;

public class PollingCardScanner implements Runnable {

	private CardTerminal cardTerminal;
	private MfCardListener cardListener;
	private MfReaderWriter readerWriter;

	public PollingCardScanner(CardTerminal cardTerminal, MfCardListener cardListener, MfReaderWriter readerWriter) {
		this.cardTerminal = cardTerminal;
		this.cardListener = cardListener;
		this.readerWriter = readerWriter;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				waitForCard(1000);
				while (cardTerminal.isCardPresent()) {
					Thread.sleep(500);
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public boolean waitForCard(int timeout) throws CardException, MfException, IOException, InterruptedException {
		if (cardTerminal.waitForCardPresent(timeout)) {
			Card card = cardTerminal.connect("*");
			CardResolver cardResolver = new CardResolver();
			MfCard mfCard = cardResolver.resolvecard(card);
			cardListener.cardDetected(mfCard, readerWriter);
			return true;
		}
		else
			return false;
	}
}

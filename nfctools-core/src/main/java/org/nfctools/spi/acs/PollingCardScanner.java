/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

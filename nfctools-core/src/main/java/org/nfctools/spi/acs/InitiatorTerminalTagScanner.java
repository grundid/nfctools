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

import org.nfctools.api.ApduTag;
import org.nfctools.api.TagScannerListener;
import org.nfctools.api.TagType;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.scio.TerminalStatus;
import org.nfctools.spi.tama.nfcip.TamaNfcIpCommunicator;

public class InitiatorTerminalTagScanner extends AbstractTerminalTagScanner implements Runnable {

	private TagScannerListener tagScannerListener;

	public InitiatorTerminalTagScanner(CardTerminal cardTerminal, TagScannerListener tagScannerListener) {
		super(cardTerminal);
		this.tagScannerListener = tagScannerListener;
	}

	public InitiatorTerminalTagScanner(CardTerminal cardTerminal) {
		this(cardTerminal, null);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			notifyStatus(TerminalStatus.WAITING);
			try {
				if (cardTerminal.waitForCardPresent(500)) {
					Card card = null;
					try {
						card = cardTerminal.connect("*");
						notifyStatus(TerminalStatus.CONNECTED);
						handleCard(card);
					}
					catch (Exception e) {
						if (tagScannerListener != null)
							tagScannerListener.onTagHandingFailed(e);
						else
							e.printStackTrace();
					}
					finally {
						waitForCardAbsent();
					}
				}
			}
			catch (CardException e) {
				if (tagScannerListener != null) {
					tagScannerListener.onScanningFailed(e);
					return;
				}
				else
					e.printStackTrace();
			}
		}
		if (tagScannerListener != null)
			tagScannerListener.onScanningEnded();
	}

	private void handleCard(Card card) {
		byte[] historicalBytes = card.getATR().getHistoricalBytes();
		TagType tagType = AcsTagUtils.identifyTagType(historicalBytes);
		AcsTag acsTag = new AcsTag(tagType, historicalBytes, card);
		if (tagType.equals(TagType.NFCIP)) {
			connectAsInitiator(acsTag);
		}
		else {
			tagListener.onTag(acsTag);
		}
	}

	private void connectAsInitiator(ApduTag tag) {
		ApduTagReaderWriter apduReaderWriter = new ApduTagReaderWriter(tag);
		TamaNfcIpCommunicator nfcIpCommunicator = new TamaNfcIpCommunicator(apduReaderWriter, apduReaderWriter);
		nfcIpCommunicator.setConnectionSetup(LlcpConstants.CONNECTION_SETUP);
		try {
			NFCIPConnection nfcipConnection = nfcIpCommunicator.connectAsInitiator();
			handleNfcipConnection(nfcipConnection);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

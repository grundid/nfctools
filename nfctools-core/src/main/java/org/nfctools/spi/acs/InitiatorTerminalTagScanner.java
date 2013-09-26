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

import org.nfctools.api.ApduTag;
import org.nfctools.api.TagType;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.scio.TerminalStatus;
import org.nfctools.spi.tama.nfcip.TamaNfcIpCommunicator;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import java.io.IOException;

public class InitiatorTerminalTagScanner extends AbstractTerminalTagScanner implements Runnable {

	public InitiatorTerminalTagScanner(CardTerminal cardTerminal) {
		super(cardTerminal);
	}

	@Override
	public void run() {
		int failureCount = 0;
		while (!Thread.interrupted() && failureCount < 5) {
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
						e.printStackTrace();
					}
					finally {
						waitForCardAbsent();
					}
				}
				failureCount = 0;
			}
			catch (CardException e) {
				failureCount++;
				e.printStackTrace();
			}
		}
		if(failureCount >= 5){
			notifyStatus(TerminalStatus.DISCONNECTED);
		}
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

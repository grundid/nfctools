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

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import org.nfctools.api.TagType;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.scio.TerminalStatus;
import org.nfctools.spi.tama.nfcip.TamaNfcIpCommunicator;

public class TargetTerminalTagScanner extends AbstractTerminalTagScanner implements Runnable {

	public TargetTerminalTagScanner(CardTerminal cardTerminal) {
		super(cardTerminal);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			notifyStatus(TerminalStatus.WAITING);

			try {
				Card card = cardTerminal.connect("direct");

				byte[] historicalBytes = card.getATR().getHistoricalBytes();
				TagType tagType = AcsTagUtils.identifyTagType(historicalBytes);

				ApduTagReaderWriter readerWriter = new ApduTagReaderWriter(new AcsDirectChannelTag(tagType,
						historicalBytes, card));

				try {
					TamaNfcIpCommunicator nfcIpCommunicator = new TamaNfcIpCommunicator(readerWriter, readerWriter);
					nfcIpCommunicator.setNfcId(LlcpConstants.nfcId3t);
					nfcIpCommunicator.setFelicaParams(LlcpConstants.felicaParams);
					nfcIpCommunicator.setMifareParams(LlcpConstants.mifareParams);
					nfcIpCommunicator.setGeneralBytes(LlcpConstants.generalBytes);
					NFCIPConnection nfcipConnection = nfcIpCommunicator.connectAsTarget();
					handleNfcipConnection(nfcipConnection);

					//					tagListener.onTag(new AcsTag(tagType, historicalBytes, card));
				}
				catch (Exception e1) {
					card.disconnect(true);
					e1.printStackTrace();
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						break;
					}
				}
				finally {
					waitForCardAbsent();
				}
			}
			catch (CardException e) {
				e.printStackTrace();
				break;
			}
		}

	}

}

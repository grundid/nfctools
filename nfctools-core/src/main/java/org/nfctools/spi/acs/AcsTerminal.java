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
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;

import org.nfctools.api.TagListener;
import org.nfctools.api.TagScannerListener;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.ndef.MfNdefReader;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.Record;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPConnectionListener;
import org.nfctools.scio.AbstractTerminal;
import org.nfctools.scio.TerminalMode;
import org.nfctools.scio.TerminalStatus;
import org.nfctools.spi.tama.nfcip.TamaNfcIpCommunicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcsTerminal extends AbstractTerminal {

	private Logger log = LoggerFactory.getLogger(getClass());
	private MfReaderWriter mfReaderWriter;
	private Thread scanningThread;
	private AbstractTerminalTagScanner tagScanner;

	@Override
	public boolean canHandle(String terminalName) {
		return terminalName.contains("ACS ACR122");
	}

	@Override
	public void registerTagListener(TagListener tagListener) {
		tagScanner.setTagListener(tagListener);
	}

	@Override
	public void setMode(TerminalMode terminalMode, TagScannerListener tagScannerListener) {
		if (TerminalMode.INITIATOR.equals(terminalMode))
			tagScanner = new InitiatorTerminalTagScanner(cardTerminal, tagScannerListener);
		else
			tagScanner = new TargetTerminalTagScanner(cardTerminal);
		scanningThread = new Thread(tagScanner);
		scanningThread.setDaemon(true);
	}

	@Override
	public void startListening() {
		scanningThread.start();
	}

	@Override
	public void stopListening() {
		scanningThread.interrupt();
	}

	@Override
	public void setNfcipConnectionListener(NFCIPConnectionListener nfcipConnectionListener) {
		if (tagScanner != null)
			tagScanner.setNfcipConnectionListener(nfcipConnectionListener);
		else
			super.setNfcipConnectionListener(nfcipConnectionListener);
	}

	@Override
	public void initInitiatorDep() throws IOException {
		mfReaderWriter = new Acr122ReaderWriter(this);
		while (!Thread.interrupted()) {
			log.info("Waiting...");
			notifyStatus(TerminalStatus.WAITING);
			try {
				if (cardTerminal.waitForCardPresent(500)) {
					Card card = null;
					try {
						card = cardTerminal.connect("*");
						byte[] historicalBytes = card.getATR().getHistoricalBytes();
						if (historicalBytes[9] == (byte)0xff && historicalBytes[10] == (byte)0x40) {
							openLlcpStack(card);
						}
						else {
							handleMfCard(card);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						if (card != null) {
							card.disconnect(true);
						}
						try {
							while (cardTerminal.isCardPresent()) {
								try {
									Thread.sleep(500);
								}
								catch (InterruptedException e) {
									break;
								}
							}
							cardTerminal.waitForCardAbsent(1000);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						notifyStatus(TerminalStatus.DISCONNECTED);
					}
				}
			}
			catch (CardException e) {
				throw new IOException(e);
			}
		}
	}

	private void handleMfCard(Card card) throws MfException, IOException {
		CardResolver cardResolver = new CardResolver();
		MfCard mfCard = cardResolver.resolvecard(card);
		this.cardDetected(mfCard, mfReaderWriter);
	}

	private void openLlcpStack(Card card) throws IOException {
		ApduReaderWriter apduReaderWriter = new ApduReaderWriter(card, true);
		TamaNfcIpCommunicator nfcIpCommunicator = new TamaNfcIpCommunicator(apduReaderWriter, apduReaderWriter);
		nfcIpCommunicator.setNfcId(LlcpConstants.nfcId3t);
		nfcIpCommunicator.setFelicaParams(LlcpConstants.felicaParams);
		nfcIpCommunicator.setMifareParams(LlcpConstants.mifareParams);
		nfcIpCommunicator.setGeneralBytes(LlcpConstants.initiatorGeneralBytes);
		try {
			NFCIPConnection nfcipConnection = nfcIpCommunicator.connectAsInitiator();
			log.info("Connection: " + nfcipConnection);
			handleNfcipConnection(nfcipConnection);
		}
		finally {
		}
	}

	@Override
	public void cardDetected(MfCard mfCard, MfReaderWriter mfReaderWriter) throws IOException {
		MfNdefReader ndefReader = new MfNdefReader(mfReaderWriter, NdefContext.getNdefMessageDecoder());
		List<Record> records = ndefReader.readNdefMessage(mfCard);
		if (ndefListener != null) {
			ndefListener.onNdefMessages(records);
		}
	}

	@Override
	public void initTargetDep() throws IOException {
		try {
			while (true) {
				Card card = cardTerminal.connect("direct");
				ApduReaderWriter apduReaderWriter = new ApduReaderWriter(card, false);
				try {
					log.info("Waiting...");
					connectAsTarget(apduReaderWriter);
				}
				catch (Exception e1) {
					e1.printStackTrace();
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						break;
					}
				}
				finally {
					card.disconnect(true);
				}
			}
		}
		catch (CardException e) {
			throw new IOException(e);
		}
	}

	private void connectAsTarget(ApduReaderWriter apduReaderWriter) throws IOException {
		TamaNfcIpCommunicator nfcIpCommunicator = new TamaNfcIpCommunicator(apduReaderWriter, apduReaderWriter);
		nfcIpCommunicator.setNfcId(LlcpConstants.nfcId3t);
		nfcIpCommunicator.setFelicaParams(LlcpConstants.felicaParams);
		nfcIpCommunicator.setMifareParams(LlcpConstants.mifareParams);
		nfcIpCommunicator.setGeneralBytes(LlcpConstants.generalBytes);
		NFCIPConnection nfcipConnection = nfcIpCommunicator.connectAsTarget();
		log.info("Connection: " + nfcipConnection);
		handleNfcipConnection(nfcipConnection);
	}
}

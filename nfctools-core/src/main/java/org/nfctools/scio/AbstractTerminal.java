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
package org.nfctools.scio;

import java.io.IOException;

import javax.smartcardio.CardTerminal;

import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;
import org.nfctools.ndef.NdefListener;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPConnectionListener;

public abstract class AbstractTerminal implements Terminal, MfCardListener {

	protected CardTerminal cardTerminal;
	protected NFCIPConnectionListener nfcipConnectionListener;
	protected TerminalStatusListener statusListener;
	protected NdefListener ndefListener;
	protected CardTerminalToken cardTerminalToken = new CardTerminalToken();

	@Override
	public void setCardTerminal(CardTerminal cardTerminal) {
		this.cardTerminal = cardTerminal;
		cardTerminalToken.setCardTerminal(cardTerminal);
	}

	@Override
	public String getTerminalName() {
		return cardTerminal.getName();
	}

	@Override
	public CardTerminal getCardTerminal() {
		return cardTerminal;
	}

	@Override
	public void setStatusListener(TerminalStatusListener statusListener) {
		this.statusListener = statusListener;
	}

	@Override
	public void setNfcipConnectionListener(NFCIPConnectionListener nfcipConnectionListener) {
		this.nfcipConnectionListener = nfcipConnectionListener;
	}

	protected void notifyStatus(TerminalStatus status) {
		if (statusListener != null)
			statusListener.onStatusChanged(status);
	}

	protected void handleNfcipConnection(NFCIPConnection nfcipConnection) throws IOException {
		if (nfcipConnection != null && nfcipConnectionListener != null) {
			notifyStatus(TerminalStatus.CONNECTED);
			nfcipConnectionListener.onConnection(nfcipConnection);
		}
	}

	@Override
	public void setNdefListener(NdefListener ndefListener) {
		this.ndefListener = ndefListener;
	}

	@Override
	public CardTerminalToken getConnectionToken() {
		return cardTerminalToken;
	}

	@Override
	public void cardDetected(MfCard mfCard, MfReaderWriter mfReaderWriter) throws IOException {
	}

	@Override
	public void open() throws IOException {
	}

	@Override
	public void close() throws IOException {
	}

}

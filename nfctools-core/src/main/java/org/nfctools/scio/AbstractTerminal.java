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

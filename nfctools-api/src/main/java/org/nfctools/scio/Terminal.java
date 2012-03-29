package org.nfctools.scio;

import java.io.IOException;

import javax.smartcardio.CardTerminal;

import org.nfctools.io.NfcDevice;
import org.nfctools.ndef.NdefListener;
import org.nfctools.nfcip.NFCIPConnectionListener;

public interface Terminal extends NfcDevice {

	boolean canHandle(String terminalName);

	String getTerminalName();

	void initInitiatorDep() throws IOException;

	void initTargetDep() throws IOException;

	void setCardTerminal(CardTerminal cardTerminal);

	void setStatusListener(TerminalStatusListener statusListener);

	void setNfcipConnectionListener(NFCIPConnectionListener nfcipConnectionListener);

	void setNdefListener(NdefListener ndefListener);
}

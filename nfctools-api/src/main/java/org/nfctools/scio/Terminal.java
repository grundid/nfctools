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

import org.nfctools.api.TagListener;
import org.nfctools.api.TagScannerListener;
import org.nfctools.io.NfcDevice;
import org.nfctools.ndef.NdefListener;
import org.nfctools.nfcip.NFCIPConnectionListener;

public interface Terminal extends NfcDevice {

	void registerTagListener(TagListener tagListener);

	void setStatusListener(TerminalStatusListener statusListener);

	void setNfcipConnectionListener(NFCIPConnectionListener nfcipConnectionListener);

	void setMode(TerminalMode terminalMode, TagScannerListener tagScannerListener);

	void startListening();

	void stopListening();

	boolean canHandle(String terminalName);

	String getTerminalName();

	@Deprecated
	void initInitiatorDep() throws IOException;

	@Deprecated
	void initTargetDep() throws IOException;

	@Deprecated
	void setCardTerminal(CardTerminal cardTerminal);

	@Deprecated
	CardTerminal getCardTerminal();

	@Deprecated
	void setNdefListener(NdefListener ndefListener);
}

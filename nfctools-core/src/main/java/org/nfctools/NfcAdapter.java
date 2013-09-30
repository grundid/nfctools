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
package org.nfctools;

import java.util.ArrayList;
import java.util.List;

import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagListener;
import org.nfctools.api.TagScannerListener;
import org.nfctools.api.UnknownTagListener;
import org.nfctools.nfcip.NFCIPConnectionListener;
import org.nfctools.scio.Terminal;
import org.nfctools.scio.TerminalMode;

public class NfcAdapter implements TagListener {

	private Terminal terminal;
	private List<NfcTagListener> nfcTagListeners = new ArrayList<NfcTagListener>();
	private UnknownTagListener unknownTagListener = new UnknownTagListener() {

		@Override
		public void unsupportedTag(Tag tag) {
		}
	};

	public NfcAdapter(Terminal terminal, TerminalMode terminalMode, TagScannerListener tagScannerListener) {
		this.terminal = terminal;
		setMode(terminalMode, tagScannerListener);
		terminal.registerTagListener(this);
	}

	public NfcAdapter(Terminal terminal, TerminalMode terminalMode) {
		this(terminal, terminalMode, null);
	}

	public void setNfcipConnectionListener(NFCIPConnectionListener nfcipConnectionListener) {
		terminal.setNfcipConnectionListener(nfcipConnectionListener);
	}

	public void registerTagListener(NfcTagListener nfcTagListener) {
		nfcTagListeners.add(nfcTagListener);
	}

	public void registerUnknownTagListerner(UnknownTagListener unknownTagListener) {
		this.unknownTagListener = unknownTagListener;
	}

	public void setMode(TerminalMode terminalMode, TagScannerListener tagScannerListener) {
		terminal.setMode(terminalMode, tagScannerListener);
	}

	public void startListening() {
		terminal.startListening();
	}

	public void stopListening() {
		terminal.stopListening();
	}

	@Override
	public void onTag(Tag tag) {
		boolean handlerFound = false;
		for (NfcTagListener nfcTagListener : nfcTagListeners) {
			try {
				if (nfcTagListener.canHandle(tag)) {
					nfcTagListener.handleTag(tag);
					handlerFound = true;
					break;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!handlerFound) {
			unknownTagListener.unsupportedTag(tag);
		}
	}
}

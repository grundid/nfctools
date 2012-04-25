package org.nfctools;

import java.util.ArrayList;
import java.util.List;

import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagListener;
import org.nfctools.api.UnknownTagListener;
import org.nfctools.scio.Terminal;

public class NfcAdapter implements TagListener {

	private Terminal terminal;

	private List<NfcTagListener> nfcTagListeners = new ArrayList<NfcTagListener>();
	private UnknownTagListener unknownTagListener = new UnknownTagListener() {

		@Override
		public void unsupportedTag(Tag tag) {

		}
	};

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
		terminal.registerTagListener(this);
	}

	public void registerTagListener(NfcTagListener nfcTagListener) {
		nfcTagListeners.add(nfcTagListener);
	}

	public void registerUnknownTagListerner(UnknownTagListener unknownTagListener) {
		this.unknownTagListener = unknownTagListener;
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

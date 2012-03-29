package org.nfctools.ndefpush;

import java.util.Collection;

import org.nfctools.ndef.Record;

public class WaitingNdefMessage {

	private Collection<Record> ndefRecords;
	private NdefPushFinishListener finishListener;

	public WaitingNdefMessage(Collection<Record> ndefRecords, NdefPushFinishListener finishListener) {
		this.ndefRecords = ndefRecords;
		this.finishListener = finishListener;
	}

	public NdefPushFinishListener getFinishListener() {
		return finishListener;
	}

	public Collection<Record> getNdefRecords() {
		return ndefRecords;
	}

}

package org.nfctools.test;

import java.util.Collection;

import org.nfctools.ndef.NdefListener;
import org.nfctools.ndef.NdefOperations;
import org.nfctools.ndef.Record;

public class NotifyingNdefListener implements NdefListener {

	private Object objectToNotify;
	private Collection<Record> records;

	public NotifyingNdefListener(Object objectToNotify) {
		this.objectToNotify = objectToNotify;
	}

	@Override
	public void onNdefMessages(Collection<Record> records) {
		this.records = records;
		synchronized (objectToNotify) {
			objectToNotify.notify();
		}
	}

	@Override
	public void onNdefOperations(NdefOperations ndefOperations) {
		// TODO Auto-generated method stub

	}

	public Collection<Record> getRecords() {
		return records;
	}
}

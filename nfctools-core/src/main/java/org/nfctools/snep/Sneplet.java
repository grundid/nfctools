package org.nfctools.snep;

import java.util.Collection;

import org.nfctools.ndef.Record;

public interface Sneplet {

	Collection<Record> doGet(Collection<Record> requestRecords);

	void doPut(Collection<Record> requestRecords);

	void onDisconnect();
}

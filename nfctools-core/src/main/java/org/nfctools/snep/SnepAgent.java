package org.nfctools.snep;

import java.util.Collection;

import org.nfctools.ndef.Record;

public interface SnepAgent {

	void doGet(Collection<Record> records, GetResponseListener getResponseListener);

	void doPut(Collection<Record> records);
}

package org.nfctools.snep;

import java.util.Collection;

import org.nfctools.ndef.Record;

public interface GetResponseListener {

	void onGetResponse(Collection<Record> records, SnepAgent snepAgent);

	void onFailed();
}

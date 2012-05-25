package org.nfctools.snep;

import java.util.Collection;

import org.nfctools.ndef.Record;

/**
 * Implementation specification for a SNEP server servlet. You can implement this to perform NDEF messages exchanges
 * based on SNEP 1.0 specs.
 * 
 */
public interface Sneplet {

	/**
	 * Called if a client executes a GET request.
	 * 
	 * @param requestRecords
	 * @return response records
	 */
	Collection<Record> doGet(Collection<Record> requestRecords);

	/**
	 * Called if a client executes a PUT request.
	 * 
	 * @param requestRecords
	 */
	void doPut(Collection<Record> requestRecords);

}

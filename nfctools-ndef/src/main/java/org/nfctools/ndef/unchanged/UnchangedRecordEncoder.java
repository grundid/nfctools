
package org.nfctools.ndef.unchanged;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;

public class UnchangedRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof UnchangedRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		UnchangedRecord unchangedRecord = (UnchangedRecord)record;
		
	    return new NdefRecord(NdefConstants.TNF_UNCHANGED, NdefConstants.EMPTY_BYTE_ARRAY, NdefConstants.EMPTY_BYTE_ARRAY, NdefConstants.EMPTY_BYTE_ARRAY);

	}
	
}

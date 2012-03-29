package org.nfctools.ndef.empty;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;

public class EmptyRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof EmptyRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		EmptyRecord emptyRecord = (EmptyRecord)record;
		
	    return new NdefRecord(NdefConstants.TNF_EMPTY, NdefConstants.EMPTY_BYTE_ARRAY, NdefConstants.EMPTY_BYTE_ARRAY, NdefConstants.EMPTY_BYTE_ARRAY);
	}
}

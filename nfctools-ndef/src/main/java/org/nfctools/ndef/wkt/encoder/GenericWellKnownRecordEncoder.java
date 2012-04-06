package org.nfctools.ndef.wkt.encoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.AbstractWellKnownRecord;

public class GenericWellKnownRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof AbstractWellKnownRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		AbstractWellKnownRecord abstractWellKnownRecord = (AbstractWellKnownRecord)record;
		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, false,
				abstractWellKnownRecord.getType(), NdefConstants.EMPTY_BYTE_ARRAY, NdefConstants.EMPTY_BYTE_ARRAY);
	}
}

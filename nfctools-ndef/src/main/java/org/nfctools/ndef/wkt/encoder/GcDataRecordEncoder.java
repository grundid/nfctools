package org.nfctools.ndef.wkt.encoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcDataRecord;

public class GcDataRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof GcDataRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		GcDataRecord dataRecord = (GcDataRecord)record;
		byte[] payload = messageEncoder.encode(dataRecord.getRecords());
		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, GcDataRecord.TYPE, record.getId(), payload);
	}
}

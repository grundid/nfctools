package org.nfctools.ndef.wkt.encoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcTargetRecord;

public class GcTargetRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof GcTargetRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		byte[] payload = messageEncoder.encodeSingle(((GcTargetRecord)record).getTargetIdentifier());
		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, GcTargetRecord.TYPE, record.getId(), payload);
	}
}

package org.nfctools.ndef.wkt.encoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcActionRecord;

public class GcActionRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof GcActionRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {

		GcActionRecord actionRecord = (GcActionRecord)record;

		byte[] payload = null;

		if (actionRecord.hasAction()) {
			payload = new byte[2];
			payload[0] = GcActionRecord.NUMERIC_CODE;
			payload[1] = (byte)actionRecord.getAction().getValue();
		}
		else if (actionRecord.hasActionRecord()) {
			byte[] subPayload = messageEncoder.encodeSingle(actionRecord.getActionRecord());
			payload = new byte[subPayload.length + 1];
			payload[0] = 0;
			System.arraycopy(subPayload, 0, payload, 1, subPayload.length);
		}

		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, GcActionRecord.TYPE, record.getId(), payload);

	}
}

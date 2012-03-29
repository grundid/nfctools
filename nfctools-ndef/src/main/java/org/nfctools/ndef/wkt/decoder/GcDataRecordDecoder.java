package org.nfctools.ndef.wkt.decoder;

import java.util.List;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcDataRecord;

public class GcDataRecordDecoder extends AbstractRecordDecoder<GcDataRecord> {

	public GcDataRecordDecoder() {
		super(GcDataRecord.TYPE);
	}

	@Override
	public GcDataRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		GcDataRecord dataRecord = new GcDataRecord();

		List<Record> records = messageDecoder.decodeToRecords(ndefRecord.getPayload());
		for (Record record : records) {
			dataRecord.add(record);
		}
		setIdOnRecord(ndefRecord, dataRecord);
		return dataRecord;
	}
}

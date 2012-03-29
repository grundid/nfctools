package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcTargetRecord;

public class GcTargetRecordDecoder extends AbstractRecordDecoder<GcTargetRecord> {

	public GcTargetRecordDecoder() {
		super(GcTargetRecord.TYPE);
	}

	@Override
	public GcTargetRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		Record record = messageDecoder.decodeToRecord(ndefRecord.getPayload());
		GcTargetRecord targetRecord = new GcTargetRecord(record);
		setIdOnRecord(ndefRecord, targetRecord);
		return targetRecord;
	}

}

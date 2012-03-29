package org.nfctools.ndef.wkt.decoder;

import java.util.List;

import org.nfctools.ndef.NdefMessage;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcActionRecord;
import org.nfctools.ndef.wkt.records.GcDataRecord;
import org.nfctools.ndef.wkt.records.GcTargetRecord;
import org.nfctools.ndef.wkt.records.GenericControlRecord;

public class GenericControlRecordDecoder extends AbstractRecordDecoder<GenericControlRecord> {

	public GenericControlRecordDecoder() {
		super(GenericControlRecord.TYPE);
	}

	@Override
	public GenericControlRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		byte configurationByte = ndefRecord.getPayload()[0];

		NdefMessage payloadNdefMessage = messageDecoder.decode(ndefRecord.getPayload(), 1,
				ndefRecord.getPayload().length - 1);

		GcTargetRecord target = null;
		GcActionRecord action = null;
		GcDataRecord data = null;

		List<Record> subRecords = messageDecoder.decodeToRecords(payloadNdefMessage);
		for (Record record : subRecords) {
			if (record instanceof GcTargetRecord)
				target = (GcTargetRecord)record;
			else if (record instanceof GcActionRecord)
				action = (GcActionRecord)record;
			else if (record instanceof GcDataRecord)
				data = (GcDataRecord)record;
			else
				throw new RuntimeException("unexpected record " + record.getClass().getName());
		}

		if (target == null)
			throw new IllegalArgumentException("no target record found in generic control record");

		GenericControlRecord gcr = new GenericControlRecord(target, configurationByte);
		gcr.setAction(action);
		gcr.setData(data);

		setIdOnRecord(ndefRecord, gcr);
		return gcr;
	}
}

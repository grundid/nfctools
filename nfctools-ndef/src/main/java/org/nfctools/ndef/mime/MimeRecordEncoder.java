package org.nfctools.ndef.mime;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;

public class MimeRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof MimeRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		MimeRecord mimeRecord = (MimeRecord)record;
		return new NdefRecord(NdefConstants.TNF_MIME_MEDIA, mimeRecord.getContentType().getBytes(), record.getId(),
				mimeRecord.getContentAsBytes());
	}
}

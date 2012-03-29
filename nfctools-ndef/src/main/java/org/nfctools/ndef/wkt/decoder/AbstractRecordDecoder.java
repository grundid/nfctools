package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.RecordUtils;

public abstract class AbstractRecordDecoder<T extends Record> implements RecordDecoder<T> {

	private byte[] type;

	protected AbstractRecordDecoder(byte[] type) {
		this.type = type;
	}

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		return RecordUtils.isEqualArray(ndefRecord.getType(), type);
	}

	protected void setIdOnRecord(NdefRecord ndefRecord, Record record) {
		record.setId(ndefRecord.getId());
	}
}

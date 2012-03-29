package org.nfctools.ndef.ext;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;

public class ExternalTypeEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof ExternalType;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		ExternalType externalType = (ExternalType)record;
		return new NdefRecord(NdefConstants.TNF_EXTERNAL_TYPE, externalType.getNamespace().getBytes(), record.getId(),
				externalType.getContent().getBytes());
	}
}

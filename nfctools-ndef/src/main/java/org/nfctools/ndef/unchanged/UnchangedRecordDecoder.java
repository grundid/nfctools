package org.nfctools.ndef.unchanged;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class UnchangedRecordDecoder extends AbstractRecordDecoder<UnchangedRecord> {

	public UnchangedRecordDecoder() {
		super(NdefConstants.TNF_UNCHANGED);
	}
	
	@Override
	public UnchangedRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new UnchangedRecord();
	}

}

package org.nfctools.ndef.empty;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class EmptyRecordDecoder extends AbstractRecordDecoder<EmptyRecord> {

	public EmptyRecordDecoder() {
		super(NdefConstants.TNF_EMPTY);
	}
	
	@Override
	public EmptyRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new EmptyRecord();
	}

}

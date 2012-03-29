package org.nfctools.ndef.unknown;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class UnknownRecordDecoder extends AbstractRecordDecoder<UnknownRecord> {

	public UnknownRecordDecoder() {
		super(NdefConstants.TNF_UNKNOWN);
	}
	
	@Override
	public UnknownRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new UnknownRecord();
	}

}

package org.nfctools.ndef.reserved;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class ReservedRecordDecoder extends AbstractRecordDecoder<ReservedRecord> {

	public ReservedRecordDecoder() {
		super(NdefConstants.TNF_RESERVED);
	}
	
	@Override
	public ReservedRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new ReservedRecord();
	}

}

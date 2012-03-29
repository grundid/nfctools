package org.nfctools.ndef.unknown;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.RecordDecoder;

public class UnknownRecordDecoder implements RecordDecoder<UnknownRecord> {

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		return ndefRecord.getTnf() == NdefConstants.TNF_UNKNOWN;
	}

	@Override
	public UnknownRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new UnknownRecord();
	}

}

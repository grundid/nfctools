package org.nfctools.ndef.empty;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.RecordDecoder;

public class EmptyRecordDecoder implements RecordDecoder<EmptyRecord> {

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		return ndefRecord.getTnf() == NdefConstants.TNF_EMPTY;
	}

	@Override
	public EmptyRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new EmptyRecord();
	}

}

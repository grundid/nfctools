package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.records.HandoverRequestRecord;

public class HandoverRequestRecordDecoder extends AbstractTypeRecordDecoder<HandoverRequestRecord> {

	public HandoverRequestRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN, HandoverRequestRecord.TYPE);
	}
	
	@Override
	public HandoverRequestRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new HandoverRequestRecord();
	}

}

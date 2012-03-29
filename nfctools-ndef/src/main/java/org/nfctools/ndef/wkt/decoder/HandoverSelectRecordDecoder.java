package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.records.HandoverSelectRecord;

public class HandoverSelectRecordDecoder extends AbstractTypeRecordDecoder<HandoverSelectRecord> {

	public HandoverSelectRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN, HandoverSelectRecord.TYPE);
	}
	
	@Override
	public HandoverSelectRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new HandoverSelectRecord();
	}

}
package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.records.HandoverCarrierRecord;

public class HandoverCarrierRecordDecoder extends AbstractTypeRecordDecoder<HandoverCarrierRecord> {

	public HandoverCarrierRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN, HandoverCarrierRecord.TYPE);
	}
	
	@Override
	public HandoverCarrierRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new HandoverCarrierRecord();
	}

}
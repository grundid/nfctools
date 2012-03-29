package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.records.AlternativeCarrierRecord;

public class AlternativeCarrierRecordDecoder extends AbstractTypeRecordDecoder<AlternativeCarrierRecord> {

	public AlternativeCarrierRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN, AlternativeCarrierRecord.TYPE);
	}

	@Override
	public AlternativeCarrierRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		return new AlternativeCarrierRecord();
	}

}

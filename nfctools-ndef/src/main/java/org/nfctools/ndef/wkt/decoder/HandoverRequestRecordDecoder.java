package org.nfctools.ndef.wkt.decoder;

import java.util.Arrays;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.records.HandoverRequestRecord;

public class HandoverRequestRecordDecoder implements RecordDecoder<HandoverRequestRecord> {

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		return ndefRecord.getTnf() == NdefConstants.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), HandoverRequestRecord.TYPE);
	}

	@Override
	public HandoverRequestRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new HandoverRequestRecord();
	}

}

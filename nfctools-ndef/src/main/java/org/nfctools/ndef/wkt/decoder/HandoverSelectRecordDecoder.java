package org.nfctools.ndef.wkt.decoder;

import java.util.Arrays;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.records.HandoverSelectRecord;

public class HandoverSelectRecordDecoder implements RecordDecoder<HandoverSelectRecord> {

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		return ndefRecord.getTnf() == NdefConstants.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), HandoverSelectRecord.TYPE);
	}

	@Override
	public HandoverSelectRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		return new HandoverSelectRecord();
	}

}
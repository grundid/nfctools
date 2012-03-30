package org.nfctools.ndef.auri;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractTypeRecordDecoder;
import org.nfctools.ndef.wkt.records.UriRecord;

public class AbsoluteUriRecordDecoder extends AbstractTypeRecordDecoder<AbsoluteUriRecord> {

	public AbsoluteUriRecordDecoder() {
		super(NdefConstants.TNF_ABSOLUTE_URI, UriRecord.TYPE);
	}

	@Override
	public AbsoluteUriRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

        String uri = new String(ndefRecord.getPayload(), NdefConstants.DEFAULT_CHARSET);

		return new AbsoluteUriRecord(uri);
	}

}

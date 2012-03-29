package org.nfctools.ndef.auri;

import java.nio.charset.Charset;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class AbsoluteUriRecordDecoder extends AbstractRecordDecoder<AbsoluteUriRecord> {

	public AbsoluteUriRecordDecoder() {
		super(NdefConstants.TNF_ABSOLUTE_URI);
	}

	@Override
	public AbsoluteUriRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

        String uri = new String(ndefRecord.getPayload(), Charset.forName("UTF-8"));

		return new AbsoluteUriRecord(uri);
	}

}

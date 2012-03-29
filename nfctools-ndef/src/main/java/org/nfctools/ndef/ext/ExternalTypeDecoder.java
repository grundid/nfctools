package org.nfctools.ndef.ext;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class ExternalTypeDecoder extends AbstractRecordDecoder<ExternalType> {

	public ExternalTypeDecoder() {
		super(new byte[0]);
	}

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		return ndefRecord.getTnf() == NdefConstants.TNF_EXTERNAL_TYPE;
	}

	@Override
	public ExternalType decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		return new ExternalType(new String(ndefRecord.getType()), new String(ndefRecord.getPayload()));
	}
}

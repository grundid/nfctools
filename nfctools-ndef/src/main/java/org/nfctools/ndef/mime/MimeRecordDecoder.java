package org.nfctools.ndef.mime;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class MimeRecordDecoder extends AbstractRecordDecoder<MimeRecord> {

	public MimeRecordDecoder() {
		super(new byte[0]);
	}

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		return new String(ndefRecord.getType()).contains("/");
	}

	@Override
	public MimeRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		String contentType = new String(ndefRecord.getType());

		MimeRecord mimeRecord = null;
		if (contentType.startsWith("text/"))
			mimeRecord = new TextMimeRecord(contentType, ndefRecord.getPayload());
		else
			mimeRecord = new BinaryMimeRecord(contentType, ndefRecord.getPayload());

		setIdOnRecord(ndefRecord, mimeRecord);
		return mimeRecord;
	}
}

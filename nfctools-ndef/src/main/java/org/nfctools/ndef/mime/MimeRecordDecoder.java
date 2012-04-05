/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nfctools.ndef.mime;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class MimeRecordDecoder extends AbstractRecordDecoder<MimeRecord> {

	public MimeRecordDecoder() {
		super(NdefConstants.TNF_MIME_MEDIA);
	}

	@Override
	public MimeRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		String contentType = new String(ndefRecord.getType(), NdefConstants.DEFAULT_CHARSET); // http://www.ietf.org/rfc/rfc2046.txt

		MimeRecord mimeRecord = null;
		if (contentType.startsWith("text/")) {
			mimeRecord = new TextMimeRecord(contentType, ndefRecord.getPayload());
		} else {
			mimeRecord = new BinaryMimeRecord(contentType, ndefRecord.getPayload());
		}
		setIdOnRecord(ndefRecord, mimeRecord);
		return mimeRecord;
	}
}

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
package org.nfctools.ndef.wkt.decoder;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.RecordUtils;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class TextRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {
		ByteArrayInputStream bais = new ByteArrayInputStream(payload);

		int status = bais.read();
		byte languageCodeLength = (byte)(status & TextRecord.LANGUAGE_CODE_MASK);
		String languageCode = new String(RecordUtils.getBytesFromStream(languageCodeLength, bais));

		byte[] textData = RecordUtils.getBytesFromStream(payload.length - languageCodeLength - 1, bais);
		Charset textEncoding = ((status & 0x80) != 0) ? TextRecord.UTF16 : TextRecord.UTF8;

		try {
			String text = new String(textData, textEncoding.name());
			return new TextRecord(text, textEncoding, new Locale(languageCode));
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}

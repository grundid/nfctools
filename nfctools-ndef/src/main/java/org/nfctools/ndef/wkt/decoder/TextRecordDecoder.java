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
import java.io.EOFException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.nfctools.ndef.NdefDecoderException;
import org.nfctools.ndef.NdefDecoder;
import org.nfctools.ndef.RecordUtils;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class TextRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefDecoder messageDecoder) {
		ByteArrayInputStream bais = new ByteArrayInputStream(payload);
		try {

			int status = bais.read();
			if(status < 0) {
				throw new EOFException();
			}
			byte languageCodeLength = (byte)(status & TextRecord.LANGUAGE_CODE_MASK);
			String languageCode = new String(RecordUtils.readByteArray(bais, languageCodeLength));
	
			byte[] textData = RecordUtils.readByteArray(bais, payload.length - languageCodeLength - 1);
			Charset textEncoding = ((status & 0x80) != 0) ? TextRecord.UTF16 : TextRecord.UTF8;

			String text = new String(textData, textEncoding.name());
			return new TextRecord(text, textEncoding, new Locale(languageCode));
		}
		catch (Exception e) {
			throw new NdefDecoderException("Text Record cannot be decoded", e);
		}
	}
}

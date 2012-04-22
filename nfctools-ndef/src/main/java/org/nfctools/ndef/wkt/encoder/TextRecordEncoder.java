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
package org.nfctools.ndef.wkt.encoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.nfctools.ndef.NdefEncoderException;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class TextRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {

		TextRecord textRecord = (TextRecord)wellKnownRecord;

		if(!textRecord.hasLocale()) {
			throw new NdefEncoderException("Expected locale", wellKnownRecord);
		}

		if(!textRecord.hasEncoding()) {
			throw new NdefEncoderException("Expected encoding", wellKnownRecord);
		}

		if(!textRecord.hasText()) {
			throw new NdefEncoderException("Expected text", wellKnownRecord);
		}

		Locale locale = textRecord.getLocale();

		byte[] languageData = (locale.getLanguage() + (locale.getCountry() == null || locale.getCountry().length() == 0 ? ""
				: ("-" + locale.getCountry()))).getBytes();

		if (languageData.length > TextRecord.LANGUAGE_CODE_MASK) {
			throw new NdefEncoderException("language code length longer than 2^5. this is not supported.", wellKnownRecord);
		}
		
		Charset encoding = textRecord.getEncoding();

		byte[] textData = getTextAsBytes(textRecord, encoding);
		byte[] payload = new byte[1 + languageData.length + textData.length];

		byte status = (byte)(languageData.length | (TextRecord.UTF16.equals(encoding) ? 0x80 : 0x00));
		payload[0] = status;
		System.arraycopy(languageData, 0, payload, 1, languageData.length);
		System.arraycopy(textData, 0, payload, 1 + languageData.length, textData.length);

		return payload;
	}

	private byte[] getTextAsBytes(TextRecord textRecord, Charset encoding) {
		try {
			return textRecord.getText().getBytes(encoding.name());
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}

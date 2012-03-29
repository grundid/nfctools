/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
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

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.TextRecord;

public class TextRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof TextRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {

		TextRecord textRecord = (TextRecord)record;

		Locale locale = textRecord.getLocale();

		byte[] lanuageData = (locale.getLanguage() + (locale.getCountry() == null || locale.getCountry().length() == 0 ? ""
				: ("-" + locale.getCountry()))).getBytes();

		if (lanuageData.length > TextRecord.LANGUAGE_CODE_MASK)
			throw new IllegalArgumentException("language code length longer than 2^5. this is not supported.");

		Charset encoding = textRecord.getEncoding();

		byte[] textData = getTextAsBytes(textRecord, encoding);
		byte[] payload = new byte[1 + lanuageData.length + textData.length];

		byte status = (byte)(lanuageData.length | (TextRecord.UTF16.equals(encoding) ? 0x80 : 0x00));
		payload[0] = status;
		System.arraycopy(lanuageData, 0, payload, 1, lanuageData.length);
		System.arraycopy(textData, 0, payload, 1 + lanuageData.length, textData.length);

		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, TextRecord.TYPE, record.getId(), payload);
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

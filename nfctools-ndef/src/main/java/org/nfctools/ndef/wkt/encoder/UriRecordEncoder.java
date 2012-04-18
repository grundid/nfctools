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

import org.nfctools.ndef.NdefEncoderException;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class UriRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {
		UriRecord uriRecord = (UriRecord)wellKnownRecord;
		
		if(!uriRecord.hasUri()) {
			throw new NdefEncoderException("Expected URI", wellKnownRecord);
		}

		String uri = uriRecord.getUri();
		byte[] uriAsBytes = getUriAsBytes(uri);

		int abbreviateIndex = getAbbreviateIndex(uri.toLowerCase());
		int uriCopyOffset = UriRecord.abbreviableUris[abbreviateIndex].length();
		byte[] payload = new byte[uriAsBytes.length + 1 - uriCopyOffset];
		payload[0] = (byte)abbreviateIndex;
		System.arraycopy(uriAsBytes, uriCopyOffset, payload, 1, uriAsBytes.length - uriCopyOffset);
		return payload;

	}

	private byte[] getUriAsBytes(String uri) {
		try {
			return uri.getBytes(UriRecord.DEFAULT_URI_CHARSET.name());
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private int getAbbreviateIndex(String uri) {
		int maxLength = 0;
		int abbreviateIndex = 0;
		for (int x = 1; x < UriRecord.abbreviableUris.length; x++) {

			String abbreviablePrefix = UriRecord.abbreviableUris[x];

			if (uri.startsWith(abbreviablePrefix) && abbreviablePrefix.length() > maxLength) {
				abbreviateIndex = x;
				maxLength = abbreviablePrefix.length();
			}
		}
		return abbreviateIndex;
	}
}

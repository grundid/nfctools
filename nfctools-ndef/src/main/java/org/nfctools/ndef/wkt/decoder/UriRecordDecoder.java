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

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

// TODO Any character value within the URI between (and including) 0 and 31 SHALL be recorded 
// as an error, and the URI record to be discarded. 
// Any invalid UTF-8 sequence SHALL be considered an error, and the entire URI record SHALL be discarded.
public class UriRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {
		String prefix = "";
		if (payload[0] >= UriRecord.abbreviableUris.length || payload[0] < 0) {
			throw new IllegalArgumentException("Unkown abbreviation index " + payload[0]);
		}
		else {
			prefix = UriRecord.abbreviableUris[payload[0]];
		}
		String uri = new String(payload, 1, payload.length - 1, UriRecord.DEFAULT_URI_CHARSET);
		return new UriRecord(prefix + uri);
	}
}

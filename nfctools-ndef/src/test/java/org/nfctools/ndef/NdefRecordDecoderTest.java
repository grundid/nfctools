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

package org.nfctools.ndef;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;
import org.nfctools.ndef.wkt.decoder.SmartPosterRecordDecoder;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.utils.NfcUtils;

public class NdefRecordDecoderTest {

	private String innerSmartPoster = "91010754026465546573745101365500736D733"
			+ "A2B3439313233343536373839303F626F64793D486921253230576965253230676568742532306573253230646972253346";

	@Test
	public void testDecodeNdefRecord() throws Exception {

		SmartPosterRecordDecoder decoder = new SmartPosterRecordDecoder();

		byte[] payload = NfcUtils.convertASCIIToBin(innerSmartPoster);

		Record record = decoder.decodePayload(payload, NdefContext.getNdefMessageDecoder());
		assertTrue(record instanceof SmartPosterRecord);

		SmartPosterRecord smartPosterRecord = (SmartPosterRecord)record;

		assertEquals("Test", smartPosterRecord.getTitle().getText());
		assertEquals(Locale.GERMAN.getLanguage(), smartPosterRecord.getTitle().getLocale().getLanguage());
		assertEquals("sms:+491234567890?body=Hi!%20Wie%20geht%20es%20dir%3F", smartPosterRecord.getUri().getUri());
	}
}

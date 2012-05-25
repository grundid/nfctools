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
package org.nfctools.ndef.encoder;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.decoder.SmartPosterDecoderTest;
import org.nfctools.ndef.wkt.records.Action;
import org.nfctools.ndef.wkt.records.ActionRecord;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.utils.NfcUtils;

public class SmartPosterRecordEncoderTest {

	private NdefMessageEncoder messageEncoder = NdefContext.getNdefMessageEncoder();

	private String innerSmartPoster = "D10245537091010754026465546573745101365500736D733A2B3439313233343536373839"
			+ "303F626F64793D486921253230576965253230676568742532306573253230646972253346";

	@Test
	public void testEncode() throws Exception {
		SmartPosterRecord smartPosterRecord = new SmartPosterRecord();
		smartPosterRecord.setTitle(new TextRecord("Test", Charset.forName("UTF8"), Locale.GERMAN));
		smartPosterRecord.setUri(new UriRecord("sms:+491234567890?body=Hi!%20Wie%20geht%20es%20dir%3F"));
		byte[] payload = messageEncoder.encodeSingle(smartPosterRecord);
		assertEquals(innerSmartPoster, NfcUtils.convertBinToASCII(payload));
	}

	@Test
	public void testEncode2() throws Exception {
		SmartPosterRecord smartPosterRecord = new SmartPosterRecord();
		smartPosterRecord.setTitle(new TextRecord("Title", Charset.forName("UTF8"), Locale.GERMANY));
		smartPosterRecord.setUri(new UriRecord("http://www.winfuture.de"));
		smartPosterRecord.setAction(new ActionRecord(Action.DEFAULT_ACTION));
		byte[] payload = messageEncoder.encodeSingle(smartPosterRecord);
		assertEquals(SmartPosterDecoderTest.smartPoster, NfcUtils.convertBinToASCII(payload));
	}
}

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
import org.nfctools.ndef.wkt.encoder.TextRecordEncoder;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.utils.NfcUtils;

/***
 * 
 * Implementation note: Java source files do not really specify encoding, thus special characters might get corrupted.
 */

public class TextRecordEncoderTest {

	private TextRecordEncoder encoder = new TextRecordEncoder();
	private NdefMessageEncoder messageEncoder = NdefContext.getNdefMessageEncoder();

	private static String string = new String(new char[]{84, 101, 115, 116, 246, 228, 252, 223, 214, 196, 220, 63});
	
	@Test
	public void testCreateTextRecordUtf8German() throws Exception {
		
		TextRecord textRecord = new TextRecord(string, Charset.forName("utf8"), Locale.GERMAN);
		byte[] bytes = encoder.encodePayload(textRecord, messageEncoder);
		assertEquals("02646554657374C3B6C3A4C3BCC39FC396C384C39C3F", NfcUtils.convertBinToASCII(bytes));
	}

	@Test
	public void testCreateTextRecordUtf16German() throws Exception {
		TextRecord textRecord = new TextRecord(string, Charset.forName("utf-16be"), Locale.GERMAN);
		byte[] bytes = encoder.encodePayload(textRecord, messageEncoder);
		assertEquals("826465005400650073007400F600E400FC00DF00D600C400DC003F", NfcUtils.convertBinToASCII(bytes));
	}

	@Test
	public void testCreateTextRecordUtf8English() throws Exception {
		TextRecord textRecord = new TextRecord(string, Charset.forName("utf8"), Locale.ENGLISH);
		byte[] bytes = encoder.encodePayload(textRecord, messageEncoder);
		assertEquals("02656E54657374C3B6C3A4C3BCC39FC396C384C39C3F", NfcUtils.convertBinToASCII(bytes));
	}

	@Test
	public void testCreateTextRecordUtf16English() throws Exception {
		TextRecord textRecord = new TextRecord(string, Charset.forName("utf-16be"), Locale.ENGLISH);
		byte[] bytes = encoder.encodePayload(textRecord, messageEncoder);
		assertEquals("82656E005400650073007400F600E400FC00DF00D600C400DC003F", NfcUtils.convertBinToASCII(bytes));
	}
}

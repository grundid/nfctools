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

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.encoder.UriRecordEncoder;
import org.nfctools.ndef.wkt.records.UriRecord;

public class UriRecordEncoderTest {

	private UriRecordEncoder encoder = new UriRecordEncoder();
	private NdefMessageEncoder messageEncoder = NdefContext.getNdefMessageEncoder();

	@Test
	public void testEncode() throws Exception {
		UriRecord record = new UriRecord("http://www.example.com");
		byte[] payload = encoder.encodePayload(record, messageEncoder);
		assertEquals(1, payload[0]);
		assertEquals('e', payload[1]);
	}

	@Test
	public void testEncodeLastKnownAbbreviation() throws Exception {
		UriRecord record = new UriRecord("urn:nfc:blabla");
		byte[] payload = encoder.encodePayload(record, messageEncoder);
		assertEquals(35, payload[0]);
		assertEquals('b', payload[1]);
	}

	@Test
	public void testEncodeNoAbbreviation() throws Exception {
		UriRecord record = new UriRecord("sms:+1234567890?body=Hi");
		byte[] payload = encoder.encodePayload(record, messageEncoder);
		assertEquals(0, payload[0]);
		assertEquals('s', payload[1]);
	}
}

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

package org.nfctools.ndef.decoder;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.wkt.decoder.UriRecordDecoder;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.utils.NfcUtils;

public class UriRecordDecoderTest {

	private UriRecordDecoder decoder = new UriRecordDecoder();
	private NdefMessageDecoder messageDecoder = NdefContext.getNdefMessageDecoder();

	private String payloadUri = "016578616D706C652E636F6D";

	@Test
	public void testDecode() throws Exception {
		byte[] payload = NfcUtils.convertASCIIToBin(payloadUri);
		UriRecord uriRecord = (UriRecord)decoder.decodePayload(payload, messageDecoder);
		assertEquals("http://www.example.com", uriRecord.getUri());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecodeInvalidBottomIndex() throws Exception {
		byte[] payload = NfcUtils.convertASCIIToBin(payloadUri);
		payload[0] = -1;
		decoder.decodePayload(payload, messageDecoder);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecodeInvalidTopIndex() throws Exception {
		byte[] payload = NfcUtils.convertASCIIToBin(payloadUri);
		payload[0] = 36;
		decoder.decodePayload(payload, messageDecoder);
	}

	@Test
	public void testDecodeLastKnownAbbreviation() throws Exception {
		byte[] payload = NfcUtils.convertASCIIToBin(payloadUri);
		payload[0] = 35;
		UriRecord uriRecord = (UriRecord)decoder.decodePayload(payload, messageDecoder);
		assertEquals("urn:nfc:example.com", uriRecord.getUri());
	}

}

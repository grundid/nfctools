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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.encoder.GenericControlRecordEncoderTest;
import org.nfctools.ndef.wkt.records.GenericControlRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.utils.NfcUtils;

public class GenericControlRecordDecoderTest {

	private NdefMessageDecoder messageDecoder = NdefContext.getNdefMessageDecoder();

	public static final byte[] payload = { (byte)0xd1, 0x02, 0x4a, 0x47, 0x63, 0x00, (byte)0xd1, 0x01, 0x22, 0x74,
			(byte)0xd1, 0x01, 0x1e, 0x55, 0x1d, 0x6c, 0x6f, 0x63, 0x61, 0x6c, 0x68, 0x6f, 0x73, 0x74, 0x2f, 0x41, 0x70,
			0x70, 0x6c, 0x69, 0x2f, 0x43, 0x75, 0x73, 0x74, 0x6f, 0x6d, 0x65, 0x72, 0x42, 0x6f, 0x6e, 0x75, 0x73,
			(byte)0xd1, 0x01, 0x0e, 0x61, 0x00, (byte)0xd1, 0x01, 0x09, 0x54, 0x05, 0x65, 0x6e, 0x2d, 0x55, 0x53, 0x61,
			0x64, 0x64, (byte)0xd1, 0x01, 0x0d, 0x64, (byte)0xd1, 0x01, 0x09, 0x54, 0x05, 0x65, 0x6e, 0x2d, 0x55, 0x53,
			0x35, 0x30, 0x30 };

	@Test
	public void testDecodeGenericControlRecordFromSpec() throws Exception {
		GenericControlRecord gcr = messageDecoder.decodeToRecord(payload);

		assertTrue(gcr.getTarget().getTargetIdentifier() instanceof UriRecord);

		UriRecord uriRecord = (UriRecord)gcr.getTarget().getTargetIdentifier();
		assertEquals("file://localhost/Appli/CustomerBonus", uriRecord.getUri());

		assertTrue(gcr.getAction().hasActionRecord());
		assertTrue(gcr.getAction().getActionRecord() instanceof TextRecord);

		TextRecord actionTextRecord = (TextRecord)gcr.getAction().getActionRecord();

		assertEquals("add", actionTextRecord.getText());

		assertEquals(1, gcr.getData().getRecords().size());
	}

	@Test
	public void testDecode() throws Exception {
		GenericControlRecord gcr = messageDecoder.decodeToRecord(NfcUtils
				.convertASCIIToBin(GenericControlRecordEncoderTest.encodedNdefSimple));
		assertNotNull(gcr);
	}

}

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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nfctools.utils.NfcUtils;

public class NdefMessageDecoderTest {

	private String emptyRecord = "D00000";
	private String twoEmptyRecords = "900000500000";

	private String twoEmptyRecordsMixedUp = "500000900000";
	private String emptyRecordWithBeginingOnly = "900000";

	// image/png IN HEX => 696D6167652F706E67
	// my_id IN HEX => 6D795F6964
	private String mimeMediaRecordWithLongPayloadAndId = "CB090000010005696D6167652F706E676D795F6964000000000000000000";

	private NdefMessageDecoder decoder = NdefContext.getNdefMessageDecoder();

	@Test
	public void testEmptyRecord() throws Exception {
		byte[] data = NfcUtils.convertASCIIToBin(emptyRecord);
		NdefMessage ndefMessage = decoder.decode(data);
		assertTrue(ndefMessage.getNdefRecords().length == 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMixedUpRecords() throws Exception {
		byte[] data = NfcUtils.convertASCIIToBin(twoEmptyRecordsMixedUp);
		decoder.decode(data);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyRecordWithBeginingOnly() throws Exception {
		byte[] data = NfcUtils.convertASCIIToBin(emptyRecordWithBeginingOnly);
		decoder.decode(data);
	}

	@Test
	public void testTwoEmptyRecords() throws Exception {
		byte[] data = NfcUtils.convertASCIIToBin(twoEmptyRecords);
		NdefMessage ndefMessage = decoder.decode(data);
		assertTrue(ndefMessage.getNdefRecords().length == 2);
	}

	@Test
	public void testMimeMediaRecordWithLongPayloadAndId() throws Exception {
		byte[] data = NfcUtils.convertASCIIToBin(mimeMediaRecordWithLongPayloadAndId);
		NdefMessage ndefMessage = decoder.decode(data);
		assertTrue(ndefMessage.getNdefRecords().length == 1);

		NdefRecord ndefRecord = ndefMessage.getNdefRecords()[0];

		assertArrayEquals("image/png".getBytes(), ndefRecord.getType());
		assertArrayEquals("my_id".getBytes(), ndefRecord.getId());
	}

	@Test
	public void testDecodeExternalType() throws Exception {
		byte[] data = NfcUtils.convertASCIIToBin("D40F13616E64726F69642E636F6D3A706B67"
				+ "64652E6772756E6469642E7465737431323334");
		NdefMessage ndefMessage = decoder.decode(data);
		assertTrue(ndefMessage.getNdefRecords().length == 1);

		NdefRecord ndefRecord = ndefMessage.getNdefRecords()[0];

		assertEquals("android.com:pkg", new String(ndefRecord.getType()));
		assertEquals("de.grundid.test1234", new String(ndefRecord.getPayload()));

	}
}

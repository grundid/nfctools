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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.mime.BinaryMimeRecord;
import org.nfctools.ndef.mime.MimeRecord;
import org.nfctools.ndef.wkt.handover.records.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.handover.records.AlternativeCarrierRecord.CarrierPowerState;
import org.nfctools.ndef.wkt.handover.records.CollisionResolutionRecord;
import org.nfctools.ndef.wkt.handover.records.HandoverRequestRecord;
import org.nfctools.ndef.wkt.handover.records.HandoverSelectRecord;
import org.nfctools.utils.NfcUtils;

public class HandoverDecoderTest {

	private String BLUETOOTH_HANDOVER_REQUEST_MESSAGE = "91020A487210D102046163010130005A103101"
			+ "6F6E2F766E642E626F6775732E6F6F62"
			+ "30003101078080bfA1040D080620110E01020304050607080910111213141516110F01020304050607080910111213141516";

	private NdefMessageDecoder decoder = NdefContext.getNdefMessageDecoder();

	@Test
	public void testBluetoothHandoverRequest10() throws Exception {
		List<Record> records = decoder.decodeToRecords(NfcUtils.convertASCIIToBin(BLUETOOTH_HANDOVER_REQUEST_MESSAGE));

		HandoverRequestRecord handoverRequestRecord = (HandoverRequestRecord)records.get(0);

		assertEquals(1, handoverRequestRecord.getMajorVersion());
		assertEquals(0, handoverRequestRecord.getMinorVersion());
		assertNull(handoverRequestRecord.getCollisionResolution());

		assertEquals(1, handoverRequestRecord.getAlternativeCarriers().size());

		AlternativeCarrierRecord alternativeCarrierRecord = handoverRequestRecord.getAlternativeCarriers().get(0);
		assertEquals(CarrierPowerState.Active, alternativeCarrierRecord.getCarrierPowerState());
		assertEquals("0", alternativeCarrierRecord.getCarrierDataReference());
		assertEquals(0, alternativeCarrierRecord.getAuxiliaryDataReferences().size());

		BinaryMimeRecord binaryMimeRecord = (BinaryMimeRecord)records.get(1);
		assertArrayEquals(new byte[] { 0x30 }, binaryMimeRecord.getId());

	}

	@Test
	public void testBluetoothHandoverRequest12() throws Exception {
		byte[] message = getResource("/handover/1.2/BluetoothHandoverRequestMessage.bin");

		List<Record> records = decoder.decodeToRecords(message);
		assertEquals(2, records.size());

		HandoverRequestRecord handoverRequestRecord = (HandoverRequestRecord)records.get(0);

		assertEquals(1, handoverRequestRecord.getMajorVersion());
		assertEquals(2, handoverRequestRecord.getMinorVersion());

		CollisionResolutionRecord collisionResolution = handoverRequestRecord.getCollisionResolution();
		assertEquals((0x01 << 8) | 0x02, collisionResolution.getRandomNumber());

		List<AlternativeCarrierRecord> alternativeCarriers = handoverRequestRecord.getAlternativeCarriers();
		assertEquals(1, alternativeCarriers.size());

		AlternativeCarrierRecord alternativeCarrierRecord = alternativeCarriers.get(0);
		assertEquals("0", alternativeCarrierRecord.getCarrierDataReference());
		assertFalse(alternativeCarrierRecord.hasAuxiliaryDataReferences());

		MimeRecord bluetooth = (MimeRecord)records.get(1);
		assertEquals("0", bluetooth.getKey());
	}

	@Test
	public void testBluetoothHandoverSelect12() throws Exception {
		//File file = new File("../../nfctools-fork2/nfctools-ndef/src/test/main/resources/handover/1.2/BluetoothHandoverRequestMessage.bin");

		byte[] message = getResource("/handover/1.2/BluetoothHandoverSelectMessage.bin");

		List<Record> records = decoder.decodeToRecords(message);
		assertEquals(2, records.size());

		HandoverSelectRecord handoverSelectRecord = (HandoverSelectRecord)records.get(0);

		assertEquals(1, handoverSelectRecord.getMajorVersion());
		assertEquals(2, handoverSelectRecord.getMinorVersion());

		List<AlternativeCarrierRecord> alternativeCarriers = handoverSelectRecord.getAlternativeCarriers();
		assertEquals(1, alternativeCarriers.size());

		AlternativeCarrierRecord alternativeCarrierRecord = alternativeCarriers.get(0);
		assertEquals(AlternativeCarrierRecord.CarrierPowerState.Active, alternativeCarrierRecord.getCarrierPowerState());
		assertEquals("0", alternativeCarrierRecord.getCarrierDataReference());
		assertFalse(alternativeCarrierRecord.hasAuxiliaryDataReferences());

		assertFalse(handoverSelectRecord.hasError());

		MimeRecord bluetooth = (MimeRecord)records.get(1);
		assertEquals("0", bluetooth.getKey());
	}

	@Test
	public void testBluetoothHandoverSelectTag12() throws Exception {
		byte[] message = getResource("/handover/1.2/BluetoothHandoverSelectMessageTag.bin");

		List<Record> records = decoder.decodeToRecords(message);
		assertEquals(2, records.size());

		HandoverSelectRecord handoverSelectRecord = (HandoverSelectRecord)records.get(0);

		assertEquals(1, handoverSelectRecord.getMajorVersion());
		assertEquals(2, handoverSelectRecord.getMinorVersion());

		List<AlternativeCarrierRecord> alternativeCarriers = handoverSelectRecord.getAlternativeCarriers();
		assertEquals(1, alternativeCarriers.size());

		AlternativeCarrierRecord alternativeCarrierRecord = alternativeCarriers.get(0);
		assertEquals(AlternativeCarrierRecord.CarrierPowerState.Unknown,
				alternativeCarrierRecord.getCarrierPowerState()); // note: active in figure, but unknown in table
		assertEquals("0", alternativeCarrierRecord.getCarrierDataReference());
		assertFalse(alternativeCarrierRecord.hasAuxiliaryDataReferences());

		assertFalse(handoverSelectRecord.hasError());

		MimeRecord bluetooth = (MimeRecord)records.get(1);
		assertEquals("0", bluetooth.getKey());
	}

	public byte[] getResource(String resouce) throws IOException {
		InputStream in = getClass().getResourceAsStream(resouce);

		assertNotNull(in);

		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read;
			do {
				read = in.read(buffer);

				if (read == -1) {
					break;
				}
				else {
					bout.write(buffer, 0, read);
				}
			} while (true);

			return bout.toByteArray();
		}
		finally {
			in.close();
		}
	}
}

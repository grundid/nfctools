package org.nfctools.ndef.decoder;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.mime.BinaryMimeRecord;
import org.nfctools.ndef.wkt.records.handover.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.records.handover.AlternativeCarrierRecord.CarrierPowerState;
import org.nfctools.ndef.wkt.records.handover.HandoverRequestRecord;
import org.nfctools.utils.NfcUtils;

public class HandoverDecoderTest {

	private String BLUETOOTH_HANDOVER_REQUEST_MESSAGE = "91020A487210D102046163010130005A103101"
			//			+ "6170706C69636174696F6E2F766E642E626F6775732E6F6F62"
			+ "6F6E2F766E642E626F6775732E6F6F62"
			+ "30003101078080bfA1040D080620110E01020304050607080910111213141516110F01020304050607080910111213141516";

	private NdefMessageDecoder decoder = NdefContext.getNdefMessageDecoder();

	@Test
	public void testHandoverRequestRecord() throws Exception {
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
}

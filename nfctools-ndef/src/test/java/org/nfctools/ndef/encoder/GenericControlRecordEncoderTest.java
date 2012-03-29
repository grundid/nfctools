package org.nfctools.ndef.encoder;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.decoder.GenericControlRecordDecoderTest;
import org.nfctools.ndef.wkt.records.GcActionRecord;
import org.nfctools.ndef.wkt.records.GcDataRecord;
import org.nfctools.ndef.wkt.records.GcTargetRecord;
import org.nfctools.ndef.wkt.records.GenericControlRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.utils.NfcUtils;

public class GenericControlRecordEncoderTest {

	private NdefMessageEncoder messageEncoder = NdefContext.getNdefMessageEncoder();
	public static final String encodedNdefSimple = "D10218476300D1011374D1010F55036C6F63616C686F73742F74657374";

	public static final byte[] dataBytes = { (byte)0xd1, 0x01, 0x09, 0x54, 0x05, 0x65, 0x6e, 0x2d, 0x55, 0x53, 0x35,
			0x30, 0x30 };

	@Test
	public void testEncode() throws Exception {
		GenericControlRecord gcr = new GenericControlRecord(new GcTargetRecord(new UriRecord(
				"file://localhost/Appli/CustomerBonus")));

		gcr.setAction(new GcActionRecord(new TextRecord("add", Charset.forName("utf8"), Locale.US)));
		gcr.setData(new GcDataRecord(new TextRecord("500", Locale.US)));

		byte[] encodedGcr = messageEncoder.encodeSingle(gcr);

		assertArrayEquals(GenericControlRecordDecoderTest.payload, encodedGcr);
	}

	@Test
	public void testEncodeSimple() throws Exception {
		GenericControlRecord gcr = new GenericControlRecord(new GcTargetRecord(new UriRecord("http://localhost/test")));
		byte[] encodedGcr = messageEncoder.encodeSingle(gcr);

		assertEquals(encodedNdefSimple, NfcUtils.convertBinToASCII(encodedGcr));
	}
}

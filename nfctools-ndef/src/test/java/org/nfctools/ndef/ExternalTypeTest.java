package org.nfctools.ndef;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.ndef.ext.ExternalType;
import org.nfctools.ndef.ext.ExternalTypeDecoder;
import org.nfctools.ndef.ext.ExternalTypeEncoder;

public class ExternalTypeTest {

	private ExternalTypeEncoder encoder = new ExternalTypeEncoder();
	private ExternalTypeDecoder decoder = new ExternalTypeDecoder();

	@Test
	public void testExternalTypeEncoder() throws Exception {
		ExternalType record = new ExternalType("android.com:pkg", "demo.package");
		NdefRecord ndefRecord = encoder.encodeRecord(record, null);

		assertEquals(NdefConstants.TNF_EXTERNAL_TYPE, ndefRecord.getTnf());

		assertEquals("android.com:pkg", new String(ndefRecord.getType()));
		assertEquals("demo.package", new String(ndefRecord.getPayload()));
	}

	@Test
	public void testExternalTypeDecoder() throws Exception {
		NdefRecord record = new NdefRecord(NdefConstants.TNF_EXTERNAL_TYPE, "android.com:pkg".getBytes(), new byte[0],
				"content".getBytes());

		assertTrue(decoder.canDecode(record));

		ExternalType externalType = decoder.decodeRecord(record, null);

		assertEquals("android.com:pkg", externalType.getNamespace());
		assertEquals("content", externalType.getContent());

	}
}

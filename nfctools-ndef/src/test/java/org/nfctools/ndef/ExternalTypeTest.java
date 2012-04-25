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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nfctools.ndef.ext.ExternalTypeDecoder;
import org.nfctools.ndef.ext.ExternalTypeEncoder;
import org.nfctools.ndef.ext.UnsupportedExternalTypeRecord;

public class ExternalTypeTest {

	private ExternalTypeEncoder encoder = new ExternalTypeEncoder();
	private ExternalTypeDecoder decoder = new ExternalTypeDecoder();

	@Test
	public void testExternalTypeEncoder() throws Exception {
		UnsupportedExternalTypeRecord record = new UnsupportedExternalTypeRecord("android.com:pkg", "demo.package");
		NdefRecord ndefRecord = encoder.encodeRecord(record, null);

		assertEquals(NdefConstants.TNF_EXTERNAL_TYPE, ndefRecord.getTnf());

		assertEquals("android.com:pkg", new String(ndefRecord.getType()));
		assertEquals("demo.package", new String(ndefRecord.getPayload()));
	}

	@Test
	public void testExternalTypeDecoder() throws Exception {
		String namespace = "nfctools.org";
		NdefRecord record = new NdefRecord(NdefConstants.TNF_EXTERNAL_TYPE, namespace.getBytes(), new byte[0],
				"content".getBytes());

		assertTrue(decoder.canDecode(record));

		UnsupportedExternalTypeRecord externalType = (UnsupportedExternalTypeRecord) decoder.decodeRecord(record, null);

		assertEquals(namespace, externalType.getNamespace());
		assertEquals("content", externalType.getContent());

	}
}

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.nfctools.ndef.ext.UnsupportedExternalTypeRecord;
import org.nfctools.ndef.records.UnknownRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.utils.NfcUtils;

public class NdefMessageEncoderTest {

	private NdefMessageEncoder encoder = NdefContext.getNdefMessageEncoder();

	@Test(expected = RuntimeException.class)
	public void testEncoderWithUnkownRecord() throws Exception {

		List<Record> records = new ArrayList<Record>();
		records.add(new UnknownRecord());
		encoder.encode(records);
	}

	@Test
	public void testEncodeMultiple() throws Exception {
		byte[] encode = encoder.encode(new TextRecord("R1", Locale.GERMANY), new TextRecord("R2", Locale.GERMANY));
		assertEquals("910108540564652D44455231510108540564652D44455232", NfcUtils.convertBinToASCII(encode));
	}

	@Test
	public void testEncodeSingle() throws Exception {

		byte[] single = encoder.encodeSingle(new UriRecord(
				"http://www.verylongurl.com/aljhsldfkjhasldfkhjaljhsldfkjhasldf"
						+ "khjaljhsldfkjhasldfkhjaljhsldfkjhasldfkhjaljhsldfkjhasldfkhjaljhsldfkjhasldfkhjal"
						+ "jhsldfkjhasldfkhjaljhsldfkjhasldfkhjaljhsldfkjhasldfkhjaljhsldfkjhasldfkhjaljhsldf"
						+ "kjhasldfkhjaljhsldfkjhasldfkhjaljhsldfkjhasldfkhjaljhsldfkjhasldfkhj"));

		assertEquals("C1010000011B5501766572796C6F6E6775726C2E636F6D2F616C6A68736C64666B6A6861736C6466"
				+ "6B686A616C6A68736C64666B6A6861736C64666B686A616C6A68736C64666B6A6861736C64666B68"
				+ "6A616C6A68736C64666B6A6861736C64666B686A616C6A68736C64666B6A6861736C64666B686A61"
				+ "6C6A68736C64666B6A6861736C64666B686A616C6A68736C64666B6A6861736C64666B686A616C6A"
				+ "68736C64666B6A6861736C64666B686A616C6A68736C64666B6A6861736C64666B686A616C6A6873"
				+ "6C64666B6A6861736C64666B686A616C6A68736C64666B6A6861736C64666B686A616C6A68736C64"
				+ "666B6A6861736C64666B686A616C6A68736C64666B6A6861736C64666B686A616C6A68736C64666B"
				+ "6A6861736C64666B686A", NfcUtils.convertBinToASCII(single));

	}

	@Test
	public void testEncodeExternalType() throws Exception {
		byte[] single = encoder.encodeSingle(new UnsupportedExternalTypeRecord("android.com:pkg", "de.grundid.test1234"));
		assertEquals("D40F13616E64726F69642E636F6D3A706B6764652E6772756E6469642E7465737431323334",
				NfcUtils.convertBinToASCII(single));
	}
}

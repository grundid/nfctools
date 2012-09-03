/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

/**
 * 
 * Test border cases for records
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class NdefMessageRoundtripTest {

	
	@Test
	public void testMax() throws IOException {

		NdefMessageEncoder ndefMessageEncoder = NdefContext.getNdefMessageEncoder();
		NdefMessageDecoder ndefMessageDecoder = NdefContext.getNdefMessageDecoder();

		int size = 255;
		
		// max out id
		byte[] id = new byte[size];
		for(int i = 0; i < id.length; i++) {
			id[i] = 0;
		}

		// max out type
		byte[] type = new byte[size];
		for(int i = 0; i < type.length; i++) {
			type[i] = 1;
		}

		// make payload length non-short
		byte[] payload = new byte[size*size];
		for(int i = 0; i < payload.length; i++) {
			payload[i] = 2;
		}
		
		byte tnf = 0x7;
		NdefRecord input = new NdefRecord(tnf, false, type, id, payload);
		
		// now check byte and inputstream and crosscheck
		
		// bytes
		byte[] encode = ndefMessageEncoder.encode(input);
		
		NdefMessage fromBytesMessage = ndefMessageDecoder.decode(encode);
		assertEquals(input, fromBytesMessage.getNdefRecords()[0]);
		
		// stream
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ndefMessageEncoder.encode(input, out);
		NdefMessage fromStreamMessage = ndefMessageDecoder.decode(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(input, fromStreamMessage.getNdefRecords()[0]);
		
		// cross check
		assertTrue(Arrays.equals(encode, out.toByteArray()));
	}
	
	@Test
	public void testMin() throws IOException {

		NdefMessageEncoder ndefMessageEncoder = NdefContext.getNdefMessageEncoder();
		NdefMessageDecoder ndefMessageDecoder = NdefContext.getNdefMessageDecoder();

		byte[] id = new byte[0];
		byte[] type = new byte[0];
		byte[] payload = new byte[0];
		
		byte tnf = 0x7;
		NdefRecord input = new NdefRecord(tnf, false, type, id, payload);
		
		// now check byte and inputstream and crosscheck
		
		// bytes
		byte[] encode = ndefMessageEncoder.encode(input);
		
		NdefMessage fromBytesMessage = ndefMessageDecoder.decode(encode);
		assertEquals(input, fromBytesMessage.getNdefRecords()[0]);
		
		// stream
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ndefMessageEncoder.encode(input, out);
		NdefMessage fromStreamMessage = ndefMessageDecoder.decode(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(input, fromStreamMessage.getNdefRecords()[0]);
		
		// cross check
		assertTrue(Arrays.equals(encode, out.toByteArray()));
	}

}

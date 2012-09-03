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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * NdefMessage encoder
 *
 */

public class NdefMessageEncoder {

	private static final int MAX_LENGTH_FOR_SHORT_RECORD = 255;

	public byte[] encode(NdefMessage message) {
		return encode(Arrays.asList(message.getNdefRecords()));
	}

	public void encode(NdefMessage message, OutputStream out) throws IOException {
		encode(Arrays.asList(message.getNdefRecords()), out);
	}

	public void encode(List<NdefRecord> ndefRecords, OutputStream out) throws IOException {
		for(int i = 0; i < ndefRecords.size(); i++) {
			write(ndefRecords.get(i), i == 0, i == ndefRecords.size() - 1, out);
		}
	}

	private void write(NdefRecord ndefRecord, boolean first, boolean last, OutputStream out) throws IOException {
		
		// configure header
		int header = 0;
		if(first) {
			header |= NdefConstants.MB;
		}
		if(last) {
			header |= NdefConstants.ME;
		}
		
		byte[] payload = ndefRecord.getPayload();
		
		// short record?
		if (payload.length <= MAX_LENGTH_FOR_SHORT_RECORD) {
			header |= NdefConstants.SR;
		}
		
		// id present
		byte[] id = ndefRecord.getId();
		if (id.length > 0) {
			if(id.length > 255) {
				throw new IllegalArgumentException("Id length exceeds maximum length of 255 by " + (id.length - 255));
			}

			header |= NdefConstants.IL;
		}
		
		int tnf = ndefRecord.getTnf();
		if(tnf > 0x7) {
			throw new IllegalArgumentException("Tnf is limited to 0x0 -> 0x7 range");
		}
		header |= tnf;
		
		// write header
		out.write(header);
		
		// write type length
		byte[] type = ndefRecord.getType();
		if(type.length > 255) {
			throw new IllegalArgumentException("Type length exceeds maximum length of 255 by " + (type.length - 255));
		}
		out.write((type.length >>>  0) & 0xFF);
		
		// write payload length
		if (payload.length <= MAX_LENGTH_FOR_SHORT_RECORD) {
			out.write((payload.length >>>  0) & 0xFF);
		} else {
			// note: not supporting longer payload than max int in java
			out.write((payload.length >>> 24) & 0xFF);
			out.write((payload.length >>> 16) & 0xFF);
			out.write((payload.length >>>  8) & 0xFF);
			out.write((payload.length >>>  0) & 0xFF);
		}
		
		// write id length if present
		if (id.length > 0) {
			out.write((id.length >>>  0) & 0xFF);
		}
		
		// write contents
		out.write(type);
		out.write(id);
		out.write(payload);
	}

	public void encode(NdefRecord ndefRecord, OutputStream out) throws IOException {
		write(ndefRecord, true, true, out);
	}
	
	public byte[] encode(NdefRecord ndefRecord) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			encode(ndefRecord, baos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return baos.toByteArray();
	}

	public byte[] encode(List<NdefRecord> ndefRecords) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			encode(ndefRecords, baos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return baos.toByteArray();
	}

}

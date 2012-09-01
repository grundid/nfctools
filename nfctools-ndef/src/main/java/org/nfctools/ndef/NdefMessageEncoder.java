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
			byte header = 0;
			if(i == 0) {
				header |= (byte)NdefConstants.MB;
			}
			if(i == ndefRecords.size() - 1) {
				header |= (byte)NdefConstants.ME;
			}
			writeNdefRecord(out, header, ndefRecords.get(i));
		}

	}

	public void encode(NdefRecord ndefRecord, OutputStream out) throws IOException {
		writeNdefRecord(out, (byte)(NdefConstants.ME | NdefConstants.MB), ndefRecord);
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

	private void writeNdefRecord(OutputStream baos, byte header, NdefRecord ndefRecord) throws IOException {
		writeHeader(baos, header, ndefRecord);
		baos.write(ndefRecord.getType().length);
		writePayloadLength(baos, ndefRecord.getPayload().length);
		writeIdLength(baos, ndefRecord.getId().length);
		writeBytes(baos, ndefRecord.getType());
		writeBytes(baos, ndefRecord.getId());
		writeBytes(baos, ndefRecord.getPayload());
	}

	private void writeHeader(OutputStream baos, byte header, NdefRecord ndefRecord) throws IOException {
		header = setShortRecord(header, ndefRecord);
		header = setIdLength(header, ndefRecord);
		header = setTypeNameFormat(header, ndefRecord);
		baos.write(header);
	}

	private byte setShortRecord(byte header, NdefRecord ndefRecord) {
		if (ndefRecord.getPayload().length <= MAX_LENGTH_FOR_SHORT_RECORD) {
			header |= NdefConstants.SR;
		}
		return header;
	}

	private byte setIdLength(byte header, NdefRecord ndefRecord) {
		if (ndefRecord.getId().length > 0) {
			header |= NdefConstants.IL;
		}
		return header;
	}

	private byte setTypeNameFormat(byte header, NdefRecord ndefRecord) {
		header |= ndefRecord.getTnf();
		return header;
	}

	private void writeBytes(OutputStream baos, byte[] bytes) throws IOException {
		baos.write(bytes, 0, bytes.length);
	}

	private void writeIdLength(OutputStream baos, int length) throws IOException {
		if (length > 0)
			baos.write(length);
	}

	private void writePayloadLength(OutputStream baos, int length) throws IOException {
		if (length <= MAX_LENGTH_FOR_SHORT_RECORD) {
			baos.write(length);
		}
		else {
			byte[] payloadLengthArray = new byte[4];
			payloadLengthArray[0] = (byte)(length >>> 24);
			payloadLengthArray[1] = (byte)(length >>> 16);
			payloadLengthArray[2] = (byte)(length >>> 8);
			payloadLengthArray[3] = (byte)(length & 0xff);
			baos.write(payloadLengthArray, 0, payloadLengthArray.length);
		}
	}
}

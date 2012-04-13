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
import java.util.Arrays;
import java.util.Iterator;


public class NdefMessageEncoder {

	private static final int MAX_LENGTH_FOR_SHORT_RECORD = 255;
	private NdefRecordEncoder ndefRecordEncoder;

	public NdefMessageEncoder(NdefRecordEncoder ndefRecordEncoder) {
		this.ndefRecordEncoder = ndefRecordEncoder;
	}

	public byte[] encodeSingle(Record record) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		encodeSingle(record, baos);
		return baos.toByteArray();
	}
	
	public void encodeSingle(Record record, ByteArrayOutputStream out) {
		byte header = (byte)(NdefConstants.MB | NdefConstants.ME);
		NdefRecord ndefRecord = ndefRecordEncoder.encode(record, this);
		writeNdefRecord(out, header, ndefRecord);
	}

	public byte[] encode(Record... records) {
		return encode(Arrays.asList(records));
	}

	public byte[] encode(Iterable<? extends Record> records) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		encode(records, baos);

		return baos.toByteArray();
	}

	public void encode(Iterable<? extends Record> records, ByteArrayOutputStream baos) {
		byte header = (byte)NdefConstants.MB;
		for (Iterator<? extends Record> it = records.iterator(); it.hasNext();) {
			Record record = it.next();
			header = setMessageEndIfLastRecord(it, header);

			NdefRecord ndefRecord = ndefRecordEncoder.encode(record, this);

			writeNdefRecord(baos, header, ndefRecord);
			header = 0;
		}
	}
	
	private byte setMessageEndIfLastRecord(Iterator<? extends Record> it, byte header) {
		if (!it.hasNext()) {
			header |= NdefConstants.ME;
		}
		return header;
	}

	private void writeNdefRecord(ByteArrayOutputStream baos, byte header, NdefRecord ndefRecord) {
		writeHeader(baos, header, ndefRecord);
		baos.write(ndefRecord.getType().length);
		writePayloadLength(baos, ndefRecord.getPayload().length);
		writeIdLength(baos, ndefRecord.getId().length);
		writeBytes(baos, ndefRecord.getType());
		writeBytes(baos, ndefRecord.getId());
		writeBytes(baos, ndefRecord.getPayload());
	}

	private void writeHeader(ByteArrayOutputStream baos, byte header, NdefRecord ndefRecord) {
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

	private void writeBytes(ByteArrayOutputStream baos, byte[] bytes) {
		baos.write(bytes, 0, bytes.length);
	}

	private void writeIdLength(ByteArrayOutputStream baos, int length) {
		if (length > 0)
			baos.write(length);
	}

	private void writePayloadLength(ByteArrayOutputStream baos, int length) {
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

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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


// TODO decode Chunked messages
public class NdefMessageDecoder {

	private NdefRecordDecoder ndefRecordDecoder;

	public NdefMessageDecoder(NdefRecordDecoder ndefRecordDecoder) {
		this.ndefRecordDecoder = ndefRecordDecoder;
	}

	public List<Record> decodeToRecords(byte[] payload) {
		return decodeToRecords(decode(payload));
	}

	public List<Record> decodeToRecords(NdefMessage ndefMessage) {
		List<Record> records = new ArrayList<Record>();

		for (NdefRecord ndefRecord : ndefMessage.getNdefRecords()) {
			records.add(ndefRecordDecoder.decode(ndefRecord, this));
		}
		return records;
	}

	public <T extends Record> T decodeToRecord(byte[] ndefMessage) {
		return this.<T> decodeToRecord(ndefMessage, 0, ndefMessage.length);
	}

	@SuppressWarnings("unchecked")
	public <T extends Record> T decodeToRecord(byte[] ndefMessage, int offset, int length) {
		NdefMessage message = decode(ndefMessage, offset, length);
		List<Record> records = decodeToRecords(message);
		if (records.size() == 1)
			return (T)records.get(0);
		else
			throw new RuntimeException("expected one record in payload but found: " + records.size());
	}

	public NdefMessage decode(byte[] ndefMessage) {
		return decode(ndefMessage, 0, ndefMessage.length);
	}

	public NdefMessage decode(byte[] ndefMessage, int offset, int length) {
		List<NdefRecord> records = new ArrayList<NdefRecord>();

		ByteArrayInputStream bais = new ByteArrayInputStream(ndefMessage, offset, length);

		while (bais.available() > 0) {
			int header = bais.read();
			short tnf = (short)(header & NdefConstants.TNF_MASK);

			int typeLength = bais.read();
			int payloadLength = getPayloadLength((header & NdefConstants.SR) != 0, bais);
			int idLength = getIdLength((header & NdefConstants.IL) != 0, bais);

			byte[] type = RecordUtils.getBytesFromStream(typeLength, bais);
			byte[] id = RecordUtils.getBytesFromStream(idLength, bais);
			byte[] payload = RecordUtils.getBytesFromStream(payloadLength, bais);

			if (records.isEmpty() && (header & NdefConstants.MB) == 0)
				throw new IllegalArgumentException("no Message Begin record at the begining");

			if (bais.available() == 0 && (header & NdefConstants.ME) == 0)
				throw new IllegalArgumentException("no Message End record at the end of array");

			records.add(new NdefRecord(tnf, type, id, payload));
		}
		return new NdefMessage(records.toArray(new NdefRecord[0]));
	}

	private int getIdLength(boolean idLengthPresent, ByteArrayInputStream bais) {
		if (idLengthPresent)
			return bais.read();
		else
			return 0;
	}

	private int getPayloadLength(boolean shortRecord, ByteArrayInputStream bais) {
		if (shortRecord)
			return bais.read();
		else {
			byte[] buffer = RecordUtils.getBytesFromStream(4, bais);
			return (int)(buffer[0] << 24) + (int)(buffer[1] << 16) + (int)(buffer[2] << 8) + (int)(buffer[3] & 0xff);
		}
	}
}

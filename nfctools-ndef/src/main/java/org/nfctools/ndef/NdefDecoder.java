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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NdefDecoder {

	private NdefRecordDecoder ndefRecordDecoder;
	private NdefMessageDecoder ndefMessageDecoder;
	
	public NdefDecoder(NdefRecordDecoder ndefRecordDecoder, NdefMessageDecoder ndefMessageDecoder) {
		this.ndefRecordDecoder = ndefRecordDecoder;
		this.ndefMessageDecoder = ndefMessageDecoder;
	}

	public List<Record> decodeToRecords(byte[] ndefMessage) {
		return decodeToRecords(ndefMessageDecoder.decode(ndefMessage));
	}

	public List<Record> decodeToRecords(byte[] ndefMessage, int offset, int length) {
		return decodeToRecords(ndefMessageDecoder.decode(ndefMessage, offset, length));
	}

	public List<Record> decodeToRecords(InputStream ndefMessage) throws IOException {
		return decodeToRecords(ndefMessageDecoder.decode(ndefMessage));
	}

	public List<Record> decodeToRecords(NdefMessage ndefMessage) {
		List<Record> records = new ArrayList<Record>();

		NdefRecord[] ndefRecords = ndefMessage.getNdefRecords();

		iterate: for (int i = 0; i < ndefRecords.length; i++) {

			NdefRecord ndefRecord = ndefRecords[i];

			if (ndefRecord.isChunked()) {
				// Concatenate chunked records to get the whole payload

				int payloadSize = ndefRecord.getPayloadSize();

				/**
				 * The value 0x06 (Unchanged) MUST be used in all middle record chunks and the terminating record chunk
				 * used in chunked payloads (see section 2.3.3). It MUST NOT be used in any other record. When used, the
				 * TYPE_LENGTH field MUST be zero and thus the TYPE field is omitted from the NDEF record.
				 */

				int k = i;
				do {
					k++;

					NdefRecord next = ndefRecords[k];
					if (next.getTnf() != NdefConstants.TNF_UNCHANGED) {
						// no terminating chunk?
						throw new NdefDecoderException("Expected terminating 'unchanged' record type at " + i);
					}

					// check that type is zero length
					byte[] type = ndefRecord.getType();
					if (type != null && type.length > 0) {
						throw new NdefDecoderException("Expected no record type at " + i);
					}

					payloadSize += next.getPayloadSize();

					if (!next.isChunked()) {
						// terminating chunk

						// concatenate chunked payloads into a single payload
						byte[] payload = new byte[payloadSize];

						int offset = 0;
						for (int p = i; p <= k; p++) {
							byte[] chunkPayload = ndefRecords[p].getPayload();

							System.arraycopy(chunkPayload, 0, payload, offset, chunkPayload.length);

							offset += chunkPayload.length;
						}

						// finally create unchunked record, copy tnf, type and id from first record
						NdefRecord unchunkedNdefRecord = new NdefRecord(ndefRecord.getTnf(), ndefRecord.getType(),
								ndefRecord.getId(), payload);

						records.add(ndefRecordDecoder.decode(unchunkedNdefRecord, this));

						// skip chunked packets
						i = k;
						// continue on to next record
						continue iterate;
					}
					else {
						// middle chunk
					}
				} while (i < ndefRecords.length);

				// no terminating chunk
				throw new NdefDecoderException("Expected terminating 'unchanged' record type");
			}
			else {
				records.add(ndefRecordDecoder.decode(ndefRecord, this));
			}
		}
		return records;
	}

	public <T extends Record> T decodeToRecord(byte[] ndefMessage) {
		return this.<T> decodeToRecord(ndefMessage, 0, ndefMessage.length);
	}

	@SuppressWarnings("unchecked")
	public <T extends Record> T decodeToRecord(byte[] ndefMessage, int offset, int length) {
		NdefMessage message = ndefMessageDecoder.decode(ndefMessage, offset, length);
		List<Record> records = decodeToRecords(message);
		if (records.size() == 1)
			return (T)records.get(0);
		else
			throw new IllegalArgumentException("Expected one record in payload but found: " + records.size());
	}

	public NdefRecordDecoder getNdefRecordDecoder() {
		return ndefRecordDecoder;
	}

	public NdefMessageDecoder getNdefMessageDecoder() {
		return ndefMessageDecoder;
	}



}

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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NdefMessageDecoder {

	private NdefRecordDecoder ndefRecordDecoder;
	
	public NdefMessageDecoder(NdefRecordDecoder ndefRecordDecoder) {
		this.ndefRecordDecoder = ndefRecordDecoder;
	}

	public List<Record> decodeToRecords(byte[] payload) {
		return decodeToRecords(decode(payload));
	}

	public List<Record> decodeToRecords(InputStream in) {
		try {
			return decodeToRecords(decode(in));
		} catch (EOFException e) {
			throw new NdefException("End of stream reached before 'Message End' record", e);
		} catch (IOException e) {
			throw new NdefException("Problem decoding message", e);
		}
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
						throw new IllegalArgumentException("Expected terminating 'unchanged' record type at " + i);
					}

					// check that type is zero length
					byte[] type = ndefRecord.getType();
					if (type != null && type.length > 0) {
						throw new IllegalArgumentException("Expected no record type at " + i);
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
				throw new IllegalArgumentException("Expected terminating 'unchanged' record type");
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
		NdefMessage message = decode(ndefMessage, offset, length);
		List<Record> records = decodeToRecords(message);
		if (records.size() == 1)
			return (T)records.get(0);
		else
			throw new IllegalArgumentException("expected one record in payload but found: " + records.size());
	}

	/**
	 * 
	 * Decode all records in buffer, from start to end flag
	 * 
	 * @return
	 */


	public NdefMessage decode(byte[] ndefMessage) {
		return decode(ndefMessage, 0, ndefMessage.length);
	}
	
	/**
	 * 
	 * Decode all records in buffer, from start to end flag
	 * 
	 * @param ndefMessage
	 * @param offset
	 * @param length
	 * 
	 * @return
	 */


	public NdefMessage decode(byte[] ndefMessage, int offset, int length) {
		try {
			return decode(new ByteArrayInputStream(ndefMessage, offset, length));
		} catch (EOFException e) {
			throw new NdefException("End of stream reached before 'Message End' record", e);
		} catch (IOException e) {
			throw new NdefException("Problem decoding message", e);
		}
	}
	
	/**
	 * 
	 * Decode all records in buffer, regardless of start and end flags
	 * 
	 * @return
	 */

	public NdefMessage decodeFully(byte[] ndefMessage) {
		return decodeFully(ndefMessage, 0, ndefMessage.length);
	}

	/**
	 * 
	 * Decode all records in buffer, regardless of start and end flags
	 * 
	 * @param ndefMessage
	 * @param offset
	 * @param length
	 * @return
	 */
	
	public NdefMessage decodeFully(byte[] ndefMessage, int offset, int length) {
		
		List<NdefRecord> records = new ArrayList<NdefRecord>();

		int count = offset;
		while(count < offset + length) {

			int header = ndefMessage[count++];
			if (count > offset + length) {
				throw new NdefException("Problem decoding message"); 
			}
			
			byte tnf = (byte)(header & NdefConstants.TNF_MASK);
			
			int typeLength = ndefMessage[count++];
			if (count > offset + length) {
				throw new NdefException("Problem decoding message"); 
			}

			int payloadLength;
			if((header & NdefConstants.SR) != 0) {
				payloadLength = ndefMessage[count++];
				if (count > offset + length) {
					throw new NdefException("Problem decoding message"); 
				}
			} else {
				if (count + 4 > offset + length) {
					throw new NdefException("Problem decoding message"); 
				}
				payloadLength = ((ndefMessage[count + 3] << 24) + (ndefMessage[count + 2] << 16) + (ndefMessage[count + 1] << 8) + (ndefMessage[count] << 0));
				
				count += 4;
			}
			
			int idLength;
			if((header & NdefConstants.IL) != 0) {
				idLength = ndefMessage[count++];
				if (count > offset + length) {
					throw new NdefException("Problem decoding message"); 
				}
			} else {
				idLength = 0;
			}
			boolean chunked = (header & NdefConstants.CF) != 0;

			if (count + typeLength > offset + length) {
				throw new NdefException("Problem decoding message"); 
			}
			byte[] type = new byte[typeLength];
			System.arraycopy(ndefMessage, count, type, 0, type.length);

			count += typeLength;
			
			byte[] id;
			if(idLength > 0) {

				if (count + idLength > offset + length) {
					throw new NdefException("Problem decoding message"); 
				}
				id = new byte[idLength];
				System.arraycopy(ndefMessage, count, id, 0, id.length);
				
				count += idLength;
			} else {
				id = NdefConstants.EMPTY_BYTE_ARRAY;
			}
			
			if (count + payloadLength > offset + length) {
				throw new NdefException("Problem decoding message"); 
			}
			byte[] payload = new byte[payloadLength];
			System.arraycopy(ndefMessage, count, payload, 0, payload.length);

			count += payloadLength;
			
			records.add(new NdefRecord(tnf, chunked, type, id, payload));
		}
		
		return new NdefMessage(records.toArray(new NdefRecord[records.size()]));
	}

	public NdefMessage decode(InputStream in) throws IOException {
		List<NdefRecord> records = new ArrayList<NdefRecord>();
		while (true) {
			int header = in.read();
			if (header < 0) {
				throw new EOFException();
			}
			
			if (records.isEmpty() && (header & NdefConstants.MB) == 0) {
				throw new IllegalArgumentException("no Message Begin record at the begining");
			}
				
			byte tnf = (byte)(header & NdefConstants.TNF_MASK);

			int typeLength = in.read();
			if (typeLength < 0) {
				throw new EOFException();
			}

			int payloadLength = getPayloadLength((header & NdefConstants.SR) != 0, in);
			int idLength;
			if((header & NdefConstants.IL) != 0) {
				idLength = getIdLength(in);
			} else {
				idLength = 0;
			}
			boolean chunked = (header & NdefConstants.CF) != 0;

			byte[] type = RecordUtils.readByteArray(in, typeLength);
			byte[] id;
			if(idLength > 0) {
				id = RecordUtils.readByteArray(in, idLength);
			} else {
				id = NdefConstants.EMPTY_BYTE_ARRAY;
			}
			byte[] payload = RecordUtils.readByteArray(in, payloadLength);

			records.add(new NdefRecord(tnf, chunked, type, id, payload));
			
			if ((header & NdefConstants.ME) != 0) {
				break;
			}
		}
		return new NdefMessage(records.toArray(new NdefRecord[records.size()]));
	}

	private int getIdLength(InputStream in) throws IOException {
		int length = in.read();
		
		if (length < 0) {
			throw new EOFException();
		}
		return length;
	}

	private int getPayloadLength(boolean shortRecord, InputStream in) throws IOException {
		if (shortRecord) {
			int length = in.read();
			
			if (length < 0) {
				throw new EOFException();
			}
			return length;
		} else {
	        int ch1 = in.read();
	        int ch2 = in.read();
	        int ch3 = in.read();
	        int ch4 = in.read();
	        if ((ch1 | ch2 | ch3 | ch4) < 0)
	            throw new EOFException();
	        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	    }
	}

	public List<Record> decodeToRecords(byte[] payload, int offset, int length) {
		return decodeToRecords(decode(payload, offset, length));
	}

}

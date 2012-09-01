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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * NdefMessage decoder. 
 * 
 *
 */

public class NdefMessageDecoder {
	
	/** Enforce message begin and message end flags in record tnf */
	private boolean enforceMessageBeginAndEndFlags;

	public NdefMessageDecoder(boolean enforceMessageBeginAndEndFlags) {
		super();
		this.enforceMessageBeginAndEndFlags = enforceMessageBeginAndEndFlags;
	}

	/**
	 * Decode to an {@link NdefMessage}. Must contain at least one record.
	 * 
	 * @param ndefMessage
	 * @return
	 */
	
	public NdefMessage decode(byte[] ndefMessage) {
		return decode(ndefMessage, 0, ndefMessage.length);
	}
	
	/**
	 * Decode to an {@link NdefMessage}. Must contain at least one record.
	 * 
	 * @param ndefMessage
	 * @return
	 */

	public NdefMessage decode(byte[] ndefMessage, int offset, int length) {
		if(length <= 0) {
			throw new IllegalArgumentException("Cannot decode message from " + length + " bytes");
		}
		List<NdefRecord> records = new ArrayList<NdefRecord>();

		int count = offset;
		while(count < offset + length) {

			int header = ndefMessage[count++];
			if (count > offset + length) {
				throw new NdefDecoderException("Problem decoding message"); 
			}
			
			byte tnf = (byte)(header & NdefConstants.TNF_MASK);
			
			if (enforceMessageBeginAndEndFlags && records.isEmpty() && (header & NdefConstants.MB) == 0) {
				throw new NdefDecoderException("No message begin in first record");
			}
			
			int typeLength = ndefMessage[count++];
			if (count > offset + length) {
				throw new NdefDecoderException("Problem decoding message"); 
			}

			int payloadLength;
			if((header & NdefConstants.SR) != 0) {
				payloadLength = ndefMessage[count++];
				if (count > offset + length) {
					throw new NdefDecoderException("Problem decoding message"); 
				}
			} else {
				if (count + 4 > offset + length) {
					throw new NdefDecoderException("Problem decoding message"); 
				}
				payloadLength = ((ndefMessage[count + 3] << 24) + (ndefMessage[count + 2] << 16) + (ndefMessage[count + 1] << 8) + (ndefMessage[count] << 0));
				
				count += 4;
			}
			
			int idLength;
			if((header & NdefConstants.IL) != 0) {
				idLength = ndefMessage[count++];
				if (count > offset + length) {
					throw new NdefDecoderException("Problem decoding message"); 
				}
			} else {
				idLength = 0;
			}
			boolean chunked = (header & NdefConstants.CF) != 0;

			if (count + typeLength > offset + length) {
				throw new NdefDecoderException("Problem decoding message"); 
			}
			byte[] type = new byte[typeLength];
			System.arraycopy(ndefMessage, count, type, 0, type.length);

			count += typeLength;
			
			byte[] id;
			if(idLength > 0) {

				if (count + idLength > offset + length) {
					throw new NdefDecoderException("Problem decoding message"); 
				}
				id = new byte[idLength];
				System.arraycopy(ndefMessage, count, id, 0, id.length);
				
				count += idLength;
			} else {
				id = NdefConstants.EMPTY_BYTE_ARRAY;
			}
			
			if (count + payloadLength > offset + length) {
				throw new NdefDecoderException("Problem decoding message, payload length exceeds array capacity by " + (count + payloadLength - (offset + length))); 
			}
			byte[] payload = new byte[payloadLength];
			System.arraycopy(ndefMessage, count, payload, 0, payload.length);

			count += payloadLength;
			
			records.add(new NdefRecord(tnf, chunked, type, id, payload));
			
			if (enforceMessageBeginAndEndFlags && (header & NdefConstants.ME) != 0) {
				break;
			}

		}
		
		if(enforceMessageBeginAndEndFlags) {
			if ((records.get(records.size() - 1).getTnf() & NdefConstants.ME) == 0) {
				throw new NdefDecoderException("No message end in last record");
			}
		}
		
		return new NdefMessage(records.toArray(new NdefRecord[records.size()]));
	}

	/**
	 * Decode to an {@link NdefMessage}. Must contain at least one record.
	 * 
	 * @param ndefMessage
	 * @return
	 */

	public NdefMessage decode(InputStream in) throws IOException {
		List<NdefRecord> records = new ArrayList<NdefRecord>();
		while (true) {
			int header = in.read();
			if (header < 0) {
				if(records.isEmpty()) {
					throw new IllegalArgumentException("Cannot decode message from zero bytes");
				}
				if(!enforceMessageBeginAndEndFlags) {
					// read forever loops ends here
					break;
				}
				throw new NdefDecoderException("No message end in last record");
			}
			
			if (enforceMessageBeginAndEndFlags && records.isEmpty() && (header & NdefConstants.MB) == 0) {
				throw new NdefDecoderException("No message begin in first record");
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
			
			if (enforceMessageBeginAndEndFlags && (header & NdefConstants.ME) != 0) {
				break;
			}
		}
		
		if(enforceMessageBeginAndEndFlags) {
			if ((records.get(records.size() - 1).getTnf() & NdefConstants.ME) == 0) {
				throw new NdefDecoderException("No message end in last record");
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

}

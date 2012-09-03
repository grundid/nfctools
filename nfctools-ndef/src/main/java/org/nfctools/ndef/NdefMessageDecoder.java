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

import java.io.DataInputStream;
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

			int header = (ndefMessage[count++] & 0xff);
			if (count > offset + length) {
				throw new NdefDecoderException("Problem decoding message"); 
			}
			
			byte tnf = (byte)(header & NdefConstants.TNF_MASK);
			
			if (enforceMessageBeginAndEndFlags && records.isEmpty() && (header & NdefConstants.MB) == 0) {
				throw new NdefDecoderException("No message begin in first record");
			}
			
			int typeLength = (ndefMessage[count++] & 0xff);
			if (count > offset + length) {
				throw new NdefDecoderException("Problem decoding message"); 
			}
			
			int payloadLength;
			if((header & NdefConstants.SR) != 0) {
				payloadLength = (ndefMessage[count++] & 0xff);
				if (count > offset + length) {
					throw new NdefDecoderException("Problem decoding message"); 
				}
			} else {
				if (count + 4 > offset + length) {
					throw new NdefDecoderException("Problem decoding message"); 
				}
				payloadLength = (((ndefMessage[count] & 0xff) << 24) + ((ndefMessage[count + 1]  & 0xff) << 16) + ((ndefMessage[count + 2]  & 0xff) << 8) + ((ndefMessage[count+3]  & 0xff) << 0)); // strictly speaking this is a unsigned int
				
				if(payloadLength < 0) { // MSB == 1.
					throw new NdefDecoderException("Payload lengths above " + Integer.MAX_VALUE + " not supported");
				}
								
				count += 4;
			}
			
			int idLength;
			if((header & NdefConstants.IL) != 0) {
				idLength = (ndefMessage[count++] & 0xff);
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
				throw new NdefDecoderException("Problem decoding message, payload length exceeds source array capacity by " + (count + payloadLength - (offset + length))); 
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
		DataInputStream din = new DataInputStream(in);
		while (true) {
			int header = din.read();
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

			int typeLength = din.readUnsignedByte();
			if (typeLength < 0) {
				throw new EOFException();
			}

			int payloadLength;
			if((header & NdefConstants.SR) != 0) {
				payloadLength = din.readUnsignedByte();
			} else {
				payloadLength = din.readInt(); // strictly speaking this is a unsigned int
				if(payloadLength < 0) { // MSB == 1.
					throw new NdefDecoderException("Payload lengths above " + Integer.MAX_VALUE + " not supported");
				}
			}
			int idLength;
			if((header & NdefConstants.IL) != 0) {
				idLength = din.readUnsignedByte();
			} else {
				idLength = 0;
			}
			boolean chunked = (header & NdefConstants.CF) != 0;

			byte[] type = new byte[typeLength]; 
			din.readFully(type);
			byte[] id;
			if(idLength > 0) {
				id = new byte[idLength];
				din.readFully(id);
			} else {
				id = NdefConstants.EMPTY_BYTE_ARRAY;
			}
			byte[] payload = new byte[payloadLength];
			din.readFully(payload);
			
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

}

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

/**
 * 
 * An NDEF record contains a payload described by a type, a length, and an optional identifier
 *
 */

public class NdefRecord {

	private byte tnf;
	/** An identifier that indicates the type of the payload. This specification supports URIs
	  *	[RFC 3986], MIME media type constructs [RFC 2616], as well as an NFC-specific
	  *	record type as type identifiers. 
	  */
	private byte[] type;
	/** An optional URI that can be used to identify a payload */
	private byte[] id;
	/** The application data carried within an NDEF record. */
	private byte[] payload;
	/**
	 * Application data that has been partitioned into multiple chunks each carried in a separate
	 * NDEF record, where each of these records except the last has the CF flag set to 1. This
	 * facility can be used to carry dynamically generated content for which the payload size is
	 * not known in advance or very large entities that don't fit into a single NDEF record.
	 */
	private boolean chunked = false;

	public NdefRecord(byte tnf, boolean chunked, byte[] type, byte[] id, byte[] payload) {
		this.tnf = tnf;
		this.chunked = chunked;
		this.type = type;
		this.id = id;
		this.payload = payload;
	}
	public NdefRecord(byte tnf, byte[] type, byte[] id, byte[] payload) {
		this(tnf, false, type, id, payload);
	}

	public boolean isChunked() {
		return chunked;
	}
	
	public byte getTnf() {
		return tnf;
	}

	public byte[] getType() {
		return type;
	}

	public byte[] getId() {
		return id;
	}

	public byte[] getPayload() {
		return payload;
	}
	
	public int getPayloadSize() {
		return payload.length;
	}

}

package org.nfctools.ndef.unknown.unsupported;

import java.util.Arrays;

import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;

/**
 * 
 * A record which is not supported by this system and thus is handled on a byte-buffer level.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class UnsupportedRecord extends Record {

	private byte tnf;
	
	/** An identifier that indicates the type of the payload. This specification supports URIs
	  *	[RFC 3986], MIME media type constructs [RFC 2616], as well as an NFC-specific
	  *	record type as type identifiers. 
	  */
	private byte[] type;
	
	/** The application data carried within an NDEF record. */
	private byte[] payload;

	public UnsupportedRecord(byte tnf, byte[] type, byte[] id, byte[] payload) {
		this.tnf = tnf;
		this.type = type;
		this.id = id;
		this.payload = payload;
	}
	
	public UnsupportedRecord(NdefRecord record) {
		this(record.getTnf(), record.getType(), record.getId(), record.getPayload());
	}

	public byte getTnf() {
		return tnf;
	}

	public void setTnf(byte tnf) {
		this.tnf = tnf;
	}

	public byte[] getType() {
		return type;
	}

	public void setType(byte[] type) {
		this.type = type;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(payload);
		result = prime * result + tnf;
		result = prime * result + Arrays.hashCode(type);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnsupportedRecord other = (UnsupportedRecord) obj;
		if (!Arrays.equals(payload, other.payload))
			return false;
		if (tnf != other.tnf)
			return false;
		if (!Arrays.equals(type, other.type))
			return false;
		return true;
	}

	
}

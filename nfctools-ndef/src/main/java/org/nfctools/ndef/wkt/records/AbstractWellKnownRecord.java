package org.nfctools.ndef.wkt.records;

import java.util.Arrays;

import org.nfctools.ndef.Record;

public abstract class AbstractWellKnownRecord extends Record {

	private byte[] type;

	public AbstractWellKnownRecord(byte[] type) {
		this.type = type;
	}

	public byte[] getType() {
		return type;
	}

	public void setType(byte[] type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		AbstractWellKnownRecord other = (AbstractWellKnownRecord)obj;
		if (!Arrays.equals(type, other.type))
			return false;
		return true;
	}

}

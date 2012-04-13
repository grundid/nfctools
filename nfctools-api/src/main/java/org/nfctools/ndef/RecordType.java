package org.nfctools.ndef;

import java.util.Arrays;

public class RecordType {

	private byte[] type;

	public RecordType(byte[] type) {
		this.type = type;
	}

	public RecordType(String type) {
		this.type = type.getBytes(NdefConstants.DEFAULT_CHARSET);
	}

	public byte[] getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(type);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordType other = (RecordType)obj;
		if (!Arrays.equals(type, other.type))
			return false;
		return true;
	}
}

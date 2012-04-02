package org.nfctools.ndef.wkt.records;

import org.nfctools.ndef.Record;

/**
 * @model
 */

public class AlternativeCarrierRecord extends Record {

	public static final byte[] TYPE = {0x61, 0x63};  // "ac"

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) {
			return false;
		}
		return true;
	}

}
package org.nfctools.ndef.wkt.records;

import org.nfctools.ndef.Record;

/**
 * @model
 */

public class HandoverCarrierRecord extends Record {

	public static final byte[] TYPE = {0x48, 0x63};  // "Hc"

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
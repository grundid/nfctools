package org.nfctools.ndef.wkt.records;

import org.nfctools.ndef.Record;

/**
 * @model
 */

public class HandoverRequestRecord extends Record {

	public static final byte[] TYPE =  {0x48, 0x72};  // "Hr"

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
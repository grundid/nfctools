package org.nfctools.ndef.unknown;

import org.nfctools.ndef.Record;

/**
 * @model
 */

public class UnknownRecord extends Record {

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

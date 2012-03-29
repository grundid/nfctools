package org.nfctools.ndef.unchanged;

import org.nfctools.ndef.Record;

public class UnchangedRecord extends Record {

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

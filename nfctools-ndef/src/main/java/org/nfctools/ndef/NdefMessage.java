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

import java.util.Arrays;

/**
 * The basic message construct defined by this specification. An NDEF message contains
 * one or more NDEF records
 * 
 */

public class NdefMessage {

	private NdefRecord[] ndefRecords;

	public NdefMessage(NdefRecord[] ndefRecords) {
		this.ndefRecords = ndefRecords;
	}

	public NdefRecord[] getNdefRecords() {
		return ndefRecords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(ndefRecords);
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
		NdefMessage other = (NdefMessage) obj;
		if (!Arrays.equals(ndefRecords, other.ndefRecords))
			return false;
		return true;
	}
	
	
}

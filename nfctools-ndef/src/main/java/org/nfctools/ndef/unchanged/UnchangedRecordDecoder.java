/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
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

package org.nfctools.ndef.unchanged;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class UnchangedRecordDecoder extends AbstractRecordDecoder<UnchangedRecord> {

	public UnchangedRecordDecoder() {
		super(NdefConstants.TNF_UNCHANGED);
	}

	@Override
	protected UnchangedRecord createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		
		/**
		The value 0x06 (Unchanged) MUST be used in all middle record chunks and the terminating
		record chunk used in chunked payloads (see section 2.3.3). It MUST NOT be used in any other
		record. When used, the TYPE_LENGTH field MUST be zero and thus the TYPE field is omitted
		from the NDEF record.		
		*/
		
		// check that type is zero length
		byte[] type = ndefRecord.getType();
		if(type != null && type.length > 0) {
			throw new IllegalArgumentException("Record type expected");
		}

		return new UnchangedRecord();
	}
}

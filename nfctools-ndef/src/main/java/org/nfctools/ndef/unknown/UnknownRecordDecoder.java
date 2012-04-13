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

package org.nfctools.ndef.unknown;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class UnknownRecordDecoder extends AbstractRecordDecoder<UnknownRecord> {

	public UnknownRecordDecoder() {
		super(NdefConstants.TNF_UNKNOWN);
	}

	@Override
	protected UnknownRecord createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		
		/**
		The value 0x05 (Unknown) SHOULD be used to indicate that the type of the payload is
		unknown. This is similar to the "application/octet-stream" media type defined by MIME [RFC
		2046]. When used, the TYPE_LENGTH field MUST be zero and thus the TYPE field is omitted
		from the NDEF record. Regarding implementation, it is RECOMMENDED that an NDEF parser
		receiving an NDEF record of this type, without further context to its use, provides a mechanism
		for storing but not processing the payload (see section 4.2).

		 */
		
		// check that type is zero length
		byte[] type = ndefRecord.getType();
		if(type != null && type.length > 0) {
			throw new IllegalArgumentException("Record type not expected");
		}
		
		return new UnknownRecord(ndefRecord.getPayload());
	}
}

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

package org.nfctools.ndef.wkt.handover.decoder;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.handover.records.ErrorRecord;
import org.nfctools.ndef.wkt.handover.records.ErrorRecord.ErrorReason;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class ErrorRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {
		ErrorRecord errorRecord = new ErrorRecord();

		ErrorReason errorReason = ErrorReason.toErrorReason(payload[0]);

		errorRecord.setErrorReason(errorReason);

		Number number;
		switch (errorReason) {
			case TemporaryMemoryConstraints: {
				/**
				 * An 8-bit unsigned integer that expresses the minimum number of milliseconds after which a Handover
				 * Request Message with the same number of octets might be processed successfully. The number of
				 * milliseconds SHALL be determined by the time interval between the sending of the error indication and
				 * the subsequent receipt of a Handover Request Message by the Handover Selector.
				 */

				number = new Short((short)(payload[1] & 0xFFFF));

				break;
			}
			case PermanenteMemoryConstraints: {

				/**
				 * A 32-bit unsigned integer, encoded with the most significant byte first, that indicates the maximum
				 * number of octets of an acceptable Handover Select Message. The number of octets SHALL be determined
				 * by the total length of the NDEF message, including all header information.
				 */

				number = new Long(((long)(payload[1] & 0xFF) << 24) + ((payload[2] & 0xFF) << 16)
						+ ((payload[3] & 0xFF) << 8) + ((payload[4] & 0xFF) << 0));

				break;
			}
			case CarrierSpecificConstraints: {

				/**
				 * An 8-bit unsigned integer that expresses the minimum number of milliseconds after which a Handover
				 * Request Message might be processed successfully. The number of milliseconds SHALL be determined by
				 * the time interval between the sending of the error indication and the subsequent receipt of a
				 * Handover Request Message by the Handover Selector.
				 */

				number = new Short((short)(payload[1] & 0xFFFF));

				break;
			}
			default: {
				throw new RuntimeException();
			}
		}

		errorRecord.setErrorData(number);

		return errorRecord;
	}

}

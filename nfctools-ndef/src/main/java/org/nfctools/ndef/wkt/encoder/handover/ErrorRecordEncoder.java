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

package org.nfctools.ndef.wkt.encoder.handover;

import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.records.WellKnownRecord;
import org.nfctools.ndef.wkt.records.handover.ErrorRecord;
import org.nfctools.ndef.wkt.records.handover.ErrorRecord.ErrorReason;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class ErrorRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodeRecordPayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {

		ErrorRecord errorRecord = (ErrorRecord)wellKnownRecord;

		if (!errorRecord.hasErrorReason()) {
			throw new IllegalArgumentException("Expected error reason");
		}

		if (!errorRecord.hasErrorData()) {
			throw new IllegalArgumentException("Expected error data");
		}

		ErrorReason errorReason = errorRecord.getErrorReason();

		byte[] payload;

		switch (errorReason) {
			case TemporaryMemoryConstraints: {
				/**
				 * An 8-bit unsigned integer that expresses the minimum number of milliseconds after which a Handover
				 * Request Message with the same number of octets might be processed successfully. The number of
				 * milliseconds SHALL be determined by the time interval between the sending of the error indication and
				 * the subsequent receipt of a Handover Request Message by the Handover Selector.
				 */

				payload = new byte[] { errorReason.getValue(), (byte)(errorRecord.getErrorData().shortValue() & 0xFF) };

				break;
			}
			case PermanenteMemoryConstraints: {

				/**
				 * A 32-bit unsigned integer, encoded with the most significant byte first, that indicates the maximum
				 * number of octets of an acceptable Handover Select Message. The number of octets SHALL be determined
				 * by the total length of the NDEF message, including all header information.
				 */
				long unsignedInt = errorRecord.getErrorData().longValue();
				payload = new byte[] { errorReason.getValue(), (byte)((unsignedInt >> 24) & 0xFF),
						(byte)((unsignedInt >> 16) & 0xFF), (byte)((unsignedInt >> 8) & 0xFF),
						(byte)(unsignedInt & 0xFF) };

				break;
			}
			case CarrierSpecificConstraints: {

				/**
				 * An 8-bit unsigned integer that expresses the minimum number of milliseconds after which a Handover
				 * Request Message might be processed successfully. The number of milliseconds SHALL be determined by
				 * the time interval between the sending of the error indication and the subsequent receipt of a
				 * Handover Request Message by the Handover Selector.
				 */

				payload = new byte[] { errorReason.getValue(), (byte)(errorRecord.getErrorData().shortValue() & 0xFF) };

				break;
			}
			default: {
				throw new RuntimeException();
			}
		}

		return payload;
	}

}

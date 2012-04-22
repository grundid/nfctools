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

package org.nfctools.ndef.wkt.handover.encoder;

import org.nfctools.ndef.NdefEncoderException;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.handover.records.ErrorRecord;
import org.nfctools.ndef.wkt.handover.records.ErrorRecord.ErrorReason;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class ErrorRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {

		ErrorRecord errorRecord = (ErrorRecord)wellKnownRecord;

		if (!errorRecord.hasErrorReason()) {
			throw new NdefEncoderException("Expected error reason", wellKnownRecord);
		}

		if (!errorRecord.hasErrorData()) {
			throw new NdefEncoderException("Expected error data", wellKnownRecord);
		}

		ErrorReason errorReason = errorRecord.getErrorReason();

		switch (errorReason) {
			case TemporaryMemoryConstraints: {
				/**
				 * An 8-bit unsigned integer that expresses the minimum number of milliseconds after which a Handover
				 * Request Message with the same number of octets might be processed successfully. The number of
				 * milliseconds SHALL be determined by the time interval between the sending of the error indication and
				 * the subsequent receipt of a Handover Request Message by the Handover Selector.
				 */

				return new byte[] { errorReason.getValue(), (byte)(errorRecord.getErrorData().shortValue() & 0xFF) };
			}
			case PermanenteMemoryConstraints: {

				/**
				 * A 32-bit unsigned integer, encoded with the most significant byte first, that indicates the maximum
				 * number of octets of an acceptable Handover Select Message. The number of octets SHALL be determined
				 * by the total length of the NDEF message, including all header information.
				 */
				long unsignedInt = errorRecord.getErrorData().longValue();
				return new byte[] { errorReason.getValue(), (byte)((unsignedInt >> 24) & 0xFF),
						(byte)((unsignedInt >> 16) & 0xFF), (byte)((unsignedInt >> 8) & 0xFF),
						(byte)(unsignedInt & 0xFF) };
			}
			case CarrierSpecificConstraints: {

				/**
				 * An 8-bit unsigned integer that expresses the minimum number of milliseconds after which a Handover
				 * Request Message might be processed successfully. The number of milliseconds SHALL be determined by
				 * the time interval between the sending of the error indication and the subsequent receipt of a
				 * Handover Request Message by the Handover Selector.
				 */

				return new byte[] { errorReason.getValue(), (byte)(errorRecord.getErrorData().shortValue() & 0xFF) };
			}
		}

		throw new NdefEncoderException("Unknown error reason " + errorReason, wellKnownRecord);
	}

}

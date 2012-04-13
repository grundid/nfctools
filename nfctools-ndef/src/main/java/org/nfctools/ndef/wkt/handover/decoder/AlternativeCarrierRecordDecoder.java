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

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.handover.records.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.handover.records.AlternativeCarrierRecord.CarrierPowerState;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class AlternativeCarrierRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {
		AlternativeCarrierRecord alternativeCarrierRecord = new AlternativeCarrierRecord();

		// cps
		alternativeCarrierRecord.setCarrierPowerState(CarrierPowerState.toCarrierPowerState(payload[0]));

		// carrier data reference
		short carrierDataReferenceLength = (short)payload[1];
		alternativeCarrierRecord.setCarrierDataReference(new String(payload, 2, carrierDataReferenceLength,
				NdefConstants.DEFAULT_CHARSET));

		// auxiliary data reference
		short auxiliaryDataReferenceCount = (short)payload[2 + carrierDataReferenceLength];

		int index = 2 + carrierDataReferenceLength + 1;
		for (int i = 0; i < auxiliaryDataReferenceCount; i++) {
			short auxiliaryDataReferenceLength = (short)payload[index];

			alternativeCarrierRecord.addAuxiliaryDataReference(new String(payload, index + 1,
					auxiliaryDataReferenceLength, NdefConstants.DEFAULT_CHARSET));

			index += 1 + auxiliaryDataReferenceLength;
		}

		//		if (index + 1 != payload.length) {
		//
		//			throw new IllegalArgumentException("Expected reserved end byte");
		//		}

		return alternativeCarrierRecord;
	}

}

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

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;
import org.nfctools.ndef.wkt.records.handover.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.records.handover.AlternativeCarrierRecord.CarrierPowerState;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 *
 */

public class AlternativeCarrierRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof AlternativeCarrierRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		
		AlternativeCarrierRecord alternativeCarrierRecord = (AlternativeCarrierRecord)record;

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		
		// cps
		CarrierPowerState carrierPowerState = alternativeCarrierRecord.getCarrierPowerState();
		if(carrierPowerState == null) {
			throw new IllegalArgumentException("Expected carrier power state");
		}
		bout.write(carrierPowerState.getValue() & 0x7); // 3 lsb

		// carrier data reference: 1
		String carrierDataReference = alternativeCarrierRecord.getCarrierDataReference();
		if(carrierDataReference == null) {
			throw new IllegalArgumentException("Expected carrier data reference");
		}
		byte[] carrierDataReferenceChar = carrierDataReference.getBytes(NdefConstants.DEFAULT_CHARSET);
		if(carrierDataReferenceChar.length > 255) {
			throw new IllegalArgumentException("Expected carrier data reference '" + carrierDataReference + "' <= 255 bytes");
		}
		// carrier data reference length (1)
		bout.write(carrierDataReferenceChar.length);
		// carrier data reference char
		bout.write(carrierDataReferenceChar, 0, carrierDataReferenceChar.length);
		
		// auxiliary data
		List<String> auxiliaryDataReferences = alternativeCarrierRecord.getAuxiliaryDataReferences();
		// auxiliary data reference count
		bout.write(auxiliaryDataReferences.size());
		
		for(String auxiliaryDataReference : auxiliaryDataReferences) {

			byte[] auxiliaryDataReferenceChar = auxiliaryDataReference.getBytes(NdefConstants.DEFAULT_CHARSET);
			// carrier data reference length (1)
			
			if(auxiliaryDataReferenceChar.length > 255) {
				throw new IllegalArgumentException("Expected auxiliary data reference '" + auxiliaryDataReference + "' <= 255 bytes");
			}

			bout.write(auxiliaryDataReferenceChar.length);
			// carrier data reference char
			bout.write(auxiliaryDataReferenceChar, 0, auxiliaryDataReferenceChar.length);
		}
		
		// reserved future use
		bout.write(0);
				
		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, AlternativeCarrierRecord.TYPE, record.getId(), bout.toByteArray());
	}

}

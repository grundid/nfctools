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

import java.util.List;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.handover.records.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.handover.records.CollisionResolutionRecord;
import org.nfctools.ndef.wkt.handover.records.HandoverRequestRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverRequestRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {

		HandoverRequestRecord handoverRequestRecord = new HandoverRequestRecord();

		byte minorVersion = (byte)(payload[0] & 0x0F);
		byte majorVersion = (byte)((payload[0] >> 4) & 0x0F);

		handoverRequestRecord.setMinorVersion(minorVersion);
		handoverRequestRecord.setMajorVersion(majorVersion);

		List<Record> records = messageDecoder.decodeToRecords(payload, 1, payload.length - 1);

		if (records.isEmpty()) {
			throw new IllegalArgumentException(
					"Expected collision resolution record and at least one alternative carrier");
		}

		for (int i = 0; i < records.size(); i++) {
			Record record = records.get(i);
			if (record instanceof CollisionResolutionRecord) {
				handoverRequestRecord.setCollisionResolution((CollisionResolutionRecord)record);
			}
			else if (record instanceof AlternativeCarrierRecord)
				handoverRequestRecord.add((AlternativeCarrierRecord)records.get(i));
			// An implementation SHALL silently ignore and SHALL NOT raise an error 
			// if it encounters other unknown record types.
		}

		if (handoverRequestRecord.getAlternativeCarriers().size() == 0)
			throw new IllegalArgumentException("Expected at least one alternative carrier");

		return handoverRequestRecord;
	}

}

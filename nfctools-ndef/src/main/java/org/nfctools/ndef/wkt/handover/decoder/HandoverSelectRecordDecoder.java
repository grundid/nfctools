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
import org.nfctools.ndef.wkt.handover.records.ErrorRecord;
import org.nfctools.ndef.wkt.handover.records.HandoverSelectRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverSelectRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {

		HandoverSelectRecord handoverSelectRecord = new HandoverSelectRecord();

		byte minorVersion = (byte)(payload[0] & 0x0F);
		byte majorVersion = (byte)((payload[0] >> 4) & 0x0F);

		handoverSelectRecord.setMinorVersion(minorVersion);
		handoverSelectRecord.setMajorVersion(majorVersion);

		// The Handover Selector MAY acknowledge zero, one, or more of the proposed alternative carriers at its own discretion.
		List<Record> records = messageDecoder.decodeToRecords(payload, 1, payload.length - 1);

		// Only Alternative Carrier Records and Error Records have a defined meaning in the payload of a Handover Select Record.
		// However, an implementation SHALL NOT raise an error if it encounters other record types, but SHOULD silently ignore them.
		for (int i = 0; i < records.size(); i++) {
			Record record = records.get(i);

			if (record instanceof AlternativeCarrierRecord) {
				handoverSelectRecord.add((AlternativeCarrierRecord)record);
			}
			else if (record instanceof ErrorRecord) {
				if (i == records.size() - 1) {
					handoverSelectRecord.setError((ErrorRecord)record);
				}
				else {
					// ignore
				}
			}
			else {
				// ignore
			}
		}

		return handoverSelectRecord;
	}

}
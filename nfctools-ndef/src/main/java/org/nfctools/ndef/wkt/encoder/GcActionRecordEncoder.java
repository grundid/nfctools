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
package org.nfctools.ndef.wkt.encoder;

import org.nfctools.ndef.NdefEncoderException;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.records.GcActionRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class GcActionRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {

		GcActionRecord actionRecord = (GcActionRecord)wellKnownRecord;
		byte[] payload = null;

		if (actionRecord.hasAction() && actionRecord.hasActionRecord()) {
			throw new NdefEncoderException("Expected action or action record, not both.", wellKnownRecord);
		} 

		if (actionRecord.hasAction()) {
			payload = new byte[2];
			payload[0] = GcActionRecord.NUMERIC_CODE;
			payload[1] = (byte)actionRecord.getAction().getValue();
		}
		else if (actionRecord.hasActionRecord()) {
			byte[] subPayload = messageEncoder.encodeSingle(actionRecord.getActionRecord());
			payload = new byte[subPayload.length + 1];
			payload[0] = 0;
			System.arraycopy(subPayload, 0, payload, 1, subPayload.length);
		} else {
			throw new NdefEncoderException("Expected action or action record.", wellKnownRecord);
		}

		return payload;
	}
}

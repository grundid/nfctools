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
package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.records.Action;
import org.nfctools.ndef.wkt.records.GcActionRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class GcActionRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {
		byte actionFlag = payload[0];

		GcActionRecord actionRecord = null;

		if ((actionFlag & GcActionRecord.NUMERIC_CODE) != 0) {
			Action action = Action.getActionByValue(payload[1]);
			actionRecord = new GcActionRecord(action);
		}
		else {

			Record record = messageDecoder.decodeToRecord(payload, 1, payload.length - 1);
			actionRecord = new GcActionRecord(record);
		}
		return actionRecord;
	}
}

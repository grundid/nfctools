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

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.Action;
import org.nfctools.ndef.wkt.records.GcActionRecord;

public class GcActionRecordDecoder extends AbstractTypeRecordDecoder<GcActionRecord> {

	public GcActionRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN, GcActionRecord.TYPE);
	}

	@Override
	protected GcActionRecord createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		byte actionFlag = ndefRecord.getPayload()[0];

		GcActionRecord actionRecord = null;

		if ((actionFlag & GcActionRecord.NUMERIC_CODE) != 0) {
			Action action = Action.getActionByValue(ndefRecord.getPayload()[1]);
			actionRecord = new GcActionRecord(action);
		}
		else {

			Record record = messageDecoder.decodeToRecord(ndefRecord.getPayload(), 1,
					ndefRecord.getPayload().length - 1);
			actionRecord = new GcActionRecord(record);
		}
		return actionRecord;
	}
}

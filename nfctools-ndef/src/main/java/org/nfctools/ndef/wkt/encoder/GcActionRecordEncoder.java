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

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcActionRecord;

public class GcActionRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof GcActionRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {

		GcActionRecord actionRecord = (GcActionRecord)record;

		byte[] payload = null;

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
		}

		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, GcActionRecord.TYPE, record.getId(), payload);

	}
}

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

import java.util.ArrayList;
import java.util.List;

import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class SmartPosterRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {
		SmartPosterRecord myRecord = (SmartPosterRecord)wellKnownRecord;
		return createPayload(messageEncoder, myRecord);
	}

	private byte[] createPayload(NdefMessageEncoder messageEncoder, SmartPosterRecord myRecord) {

		List<Record> records = new ArrayList<Record>();

		if (myRecord.getTitle() != null)
			records.add(myRecord.getTitle());
		if (myRecord.getUri() != null)
			records.add(myRecord.getUri());
		if (myRecord.getAction() != null)
			records.add(myRecord.getAction());

		return messageEncoder.encode(records);
	}

}

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

package org.nfctools.ndef.wkt.decoder;

import java.util.List;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.ActionRecord;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;

public class SmartPosterDecoder extends AbstractRecordDecoder<SmartPosterRecord> {

	public SmartPosterDecoder() {
		super(SmartPosterRecord.TYPE);
	}

	@Override
	public SmartPosterRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		SmartPosterRecord smartPosterRecord = new SmartPosterRecord();

		List<Record> records = messageDecoder.decodeToRecords(messageDecoder.decode(ndefRecord.getPayload()));

		for (Record record : records) {

			if (record instanceof UriRecord) {
				smartPosterRecord.setUri((UriRecord)record);
			}
			else if (record instanceof TextRecord) {
				smartPosterRecord.setTitle((TextRecord)record);
			}
			else if (record instanceof ActionRecord) {
				smartPosterRecord.setAction((ActionRecord)record);
			}
		}

		setIdOnRecord(ndefRecord, smartPosterRecord);
		return smartPosterRecord;
	}

}

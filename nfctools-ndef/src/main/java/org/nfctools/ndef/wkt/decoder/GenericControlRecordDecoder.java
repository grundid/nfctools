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

import java.util.List;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessage;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcActionRecord;
import org.nfctools.ndef.wkt.records.GcDataRecord;
import org.nfctools.ndef.wkt.records.GcTargetRecord;
import org.nfctools.ndef.wkt.records.GenericControlRecord;

public class GenericControlRecordDecoder extends AbstractTypeRecordDecoder<GenericControlRecord> {

	public GenericControlRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN, GenericControlRecord.TYPE);
	}

	@Override
	public GenericControlRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		byte configurationByte = ndefRecord.getPayload()[0];

		NdefMessage payloadNdefMessage = messageDecoder.decode(ndefRecord.getPayload(), 1,
				ndefRecord.getPayload().length - 1);

		GcTargetRecord target = null;
		GcActionRecord action = null;
		GcDataRecord data = null;

		List<Record> subRecords = messageDecoder.decodeToRecords(payloadNdefMessage);
		for (Record record : subRecords) {
			if (record instanceof GcTargetRecord)
				target = (GcTargetRecord)record;
			else if (record instanceof GcActionRecord)
				action = (GcActionRecord)record;
			else if (record instanceof GcDataRecord)
				data = (GcDataRecord)record;
			else
				throw new RuntimeException("unexpected record " + record.getClass().getName());
		}

		if (target == null)
			throw new IllegalArgumentException("no target record found in generic control record");

		GenericControlRecord gcr = new GenericControlRecord(target, configurationByte);
		gcr.setAction(action);
		gcr.setData(data);

		setIdOnRecord(ndefRecord, gcr);
		return gcr;
	}
}

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
import java.util.ArrayList;
import java.util.List;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;
import org.nfctools.ndef.wkt.records.handover.HandoverRequestRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 *
 */

public class HandoverRequestRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof HandoverRequestRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		
		HandoverRequestRecord handoverRequestRecord = (HandoverRequestRecord)record;
		
		ByteArrayOutputStream payload = new ByteArrayOutputStream();
		
		// major version, minor version
		payload.write((handoverRequestRecord.getMajorVersion() << 4) | handoverRequestRecord.getMinorVersion());
		
		if(!handoverRequestRecord.hasCollisionResolution()) {
			throw new IllegalArgumentException("Expected collision resolution");
		}
		
		// implementation note: write alternative carriers and and collision resolution together
		if(!handoverRequestRecord.hasAlternativeCarriers()) {
			// At least a single alternative carrier MUST be specified by the Handover Requester.
			throw new IllegalArgumentException("Expected at least one alternative carrier");
		}
		List<Record> records = new ArrayList<Record>();
		
		// a collision resolution record
		records.add(handoverRequestRecord.getCollisionResolution());
		
		// n alternative carrier records
		records.addAll(handoverRequestRecord.getAlternativeCarriers());
		
		messageEncoder.encode(records, payload);

		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, HandoverRequestRecord.TYPE, record.getId(), payload.toByteArray());
	}

}

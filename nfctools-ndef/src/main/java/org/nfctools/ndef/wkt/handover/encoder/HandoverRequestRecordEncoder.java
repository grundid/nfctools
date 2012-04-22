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

package org.nfctools.ndef.wkt.handover.encoder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.nfctools.ndef.NdefEncoderException;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.handover.records.HandoverRequestRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverRequestRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {

		HandoverRequestRecord handoverRequestRecord = (HandoverRequestRecord)wellKnownRecord;

		ByteArrayOutputStream payload = new ByteArrayOutputStream();

		// major version, minor version
		payload.write((handoverRequestRecord.getMajorVersion() << 4) | handoverRequestRecord.getMinorVersion());

		if (!handoverRequestRecord.hasCollisionResolution()) {
			throw new NdefEncoderException("Expected collision resolution", handoverRequestRecord);
		}

		// implementation note: write alternative carriers and and collision resolution together
		if (!handoverRequestRecord.hasAlternativeCarriers()) {
			// At least a single alternative carrier MUST be specified by the Handover Requester.
			throw new NdefEncoderException("Expected at least one alternative carrier", handoverRequestRecord);
		}
		List<Record> records = new ArrayList<Record>();

		// a collision resolution record
		records.add(handoverRequestRecord.getCollisionResolution());

		// n alternative carrier records
		records.addAll(handoverRequestRecord.getAlternativeCarriers());

		messageEncoder.encode(records, payload);

		return payload.toByteArray();
	}

}

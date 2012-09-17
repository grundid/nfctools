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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nfctools.ndef.NdefEncoder;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.handover.records.HandoverSelectRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverSelectRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefEncoder messageEncoder) {

		HandoverSelectRecord handoverSelectRecord = (HandoverSelectRecord)wellKnownRecord;

		ByteArrayOutputStream payload = new ByteArrayOutputStream();

		// major version, minor version
		payload.write((handoverSelectRecord.getMajorVersion() << 4) | handoverSelectRecord.getMinorVersion());

		// implementation note: write alternative carriers and error record together
		if (handoverSelectRecord.hasError() && handoverSelectRecord.hasAlternativeCarriers()) {

			List<Record> records = new ArrayList<Record>();

			// n alternative carrier records
			records.addAll(handoverSelectRecord.getAlternativeCarriers());

			// an error message
			records.add(handoverSelectRecord.getError());

			try {
				messageEncoder.encode(records, payload);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if (handoverSelectRecord.hasAlternativeCarriers()) {

			// n alternative carrier records
			try {
				messageEncoder.encode(handoverSelectRecord.getAlternativeCarriers(), payload);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if (handoverSelectRecord.hasError()) {

			// an error message
			try {
				messageEncoder.encode(handoverSelectRecord.getError(), payload);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else {
			// do nothing
		}

		return payload.toByteArray();
	}

}

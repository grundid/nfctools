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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class SmartPosterRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodeRecordPayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {
		SmartPosterRecord myRecord = (SmartPosterRecord)wellKnownRecord;
		return createPayload(messageEncoder, myRecord);
	}

	private byte[] createPayload(NdefMessageEncoder messageEncoder, SmartPosterRecord myRecord) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			if (myRecord.getTitle() != null)
				baos.write(messageEncoder.encodeSingle(myRecord.getTitle()));
			if (myRecord.getUri() != null)
				baos.write(messageEncoder.encodeSingle(myRecord.getUri()));
			if (myRecord.getAction() != null)
				baos.write(messageEncoder.encodeSingle(myRecord.getAction()));

			return baos.toByteArray();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}

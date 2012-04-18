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

import org.nfctools.ndef.NdefEncoderException;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.records.GenericControlRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class GenericControlRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {
		GenericControlRecord myRecord = (GenericControlRecord)wellKnownRecord;

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			baos.write(myRecord.getConfigurationByte());

			if(!myRecord.hasTarget()) {
				throw new NdefEncoderException("Expected target", myRecord);
			}
			baos.write(messageEncoder.encodeSingle(myRecord.getTarget()));

			if (myRecord.getAction() != null)
				baos.write(messageEncoder.encodeSingle(myRecord.getAction()));
			if (myRecord.getData() != null)
				baos.write(messageEncoder.encodeSingle(myRecord.getData()));

			return baos.toByteArray();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}

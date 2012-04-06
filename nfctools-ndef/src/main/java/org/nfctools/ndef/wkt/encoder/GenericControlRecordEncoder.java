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

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GenericControlRecord;

public class GenericControlRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof GenericControlRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {

		GenericControlRecord myRecord = (GenericControlRecord)record;

		byte[] subPayload = createSubPayload(messageEncoder, myRecord);

		byte[] payload = new byte[subPayload.length + 1];
		payload[0] = myRecord.getConfigurationByte();
		System.arraycopy(subPayload, 0, payload, 1, subPayload.length);

		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, GenericControlRecord.TYPE, record.getId(), payload);
	}

	private byte[] createSubPayload(NdefMessageEncoder messageEncoder, GenericControlRecord myRecord) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
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

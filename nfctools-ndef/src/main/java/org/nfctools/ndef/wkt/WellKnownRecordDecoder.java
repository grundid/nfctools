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
package org.nfctools.ndef.wkt;

import java.util.HashMap;
import java.util.Map;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.RecordType;
import org.nfctools.ndef.wkt.decoder.RecordDecoder;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class WellKnownRecordDecoder implements RecordDecoder<WellKnownRecord> {

	private Map<RecordType, WellKnownRecordConfig> recordDecoders = new HashMap<RecordType, WellKnownRecordConfig>();

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		if(NdefConstants.TNF_WELL_KNOWN == ndefRecord.getTnf()) {
			return recordDecoders.containsKey(new RecordType(ndefRecord.getType()));
		}
		return false;
	}

	@Override
	public WellKnownRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		WellKnownRecordConfig config = recordDecoders.get(new RecordType(ndefRecord.getType()));
		if (config != null) {
			WellKnownRecordPayloadDecoder payloadDecoder = config.getPayloadDecoder();
			WellKnownRecord record = payloadDecoder.decodePayload(ndefRecord.getPayload(), messageDecoder);
			record.setId(ndefRecord.getId());
			return record;
		}
		else
			throw new IllegalArgumentException("Unsupported Well Known NDEF Type [" + new String(ndefRecord.getType())
					+ "]");
	}

	public void addRecordConfig(WellKnownRecordConfig config) {
		recordDecoders.put(config.getRecordType(), config);
	}
}

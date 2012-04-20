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
package org.nfctools.ndef.ext;

import java.util.HashMap;
import java.util.Map;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefException;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.WellKnownRecordConfig;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;

public class ExternalTypeEncoder implements RecordEncoder {

	private Map<Class<?>, ExternalTypeRecordConfig> externalRecordTypes = new HashMap<Class<?>, ExternalTypeRecordConfig>();

	@Override
	public boolean canEncode(Record record) {
		return record instanceof ExternalTypeRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		ExternalTypeRecord externalType = (ExternalTypeRecord)record;
		
		ExternalTypeRecordConfig config = externalRecordTypes.get(record.getClass());
		
		byte[] payload;
		if(config != null) {
			payload = config.getContentEncoder().encodeContent(externalType).getBytes(NdefConstants.DEFAULT_CHARSET);
		} else if(externalType instanceof UnsupportedExternalTypeRecord){
			UnsupportedExternalTypeRecord externalTypeUnsupportedRecord = (UnsupportedExternalTypeRecord)externalType;
			payload = externalTypeUnsupportedRecord.getContent().getBytes(NdefConstants.DEFAULT_CHARSET);
		} else {
			throw new IllegalArgumentException("Unable to encode external type " + externalType.getClass().getName()); // TODO change to ndef exception
		}
		byte[] type = externalType.getNamespace().getBytes(NdefConstants.DEFAULT_CHARSET);
		return new NdefRecord(NdefConstants.TNF_EXTERNAL_TYPE, type, record.getId(), payload);
	}
	
	public void addRecordConfig(ExternalTypeRecordConfig config) {
		externalRecordTypes.put(config.getRecordClass(), config);
	}
}

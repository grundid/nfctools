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
import java.util.Locale;
import java.util.Map;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefEncoderException;
import org.nfctools.ndef.NdefEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;

public class ExternalTypeEncoder implements RecordEncoder {

	private Map<Class<?>, ExternalTypeRecordConfig> externalRecordTypes = new HashMap<Class<?>, ExternalTypeRecordConfig>();

	@Override
	public boolean canEncode(Record record) {
		return record instanceof ExternalTypeRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefEncoder messageEncoder) {
		ExternalTypeRecord externalType = (ExternalTypeRecord)record;
		
		ExternalTypeRecordConfig config = externalRecordTypes.get(record.getClass());
		
		String domainAndType;
		byte[] payload;
		if(config != null) {
			domainAndType = config.getDomainAndType();
			payload = config.getContentEncoder().encodeContent(externalType).getBytes(NdefConstants.DEFAULT_CHARSET);
			
		} else if(externalType instanceof UnsupportedExternalTypeRecord){
			UnsupportedExternalTypeRecord externalTypeUnsupportedRecord = (UnsupportedExternalTypeRecord)externalType;
			
			if(!externalTypeUnsupportedRecord.hasData()) {
				throw new NdefEncoderException("Expected content", record);
			}
			if(!externalTypeUnsupportedRecord.hasDomain()) {
				throw new NdefEncoderException("Expected domain", record);
			}
			if(!externalTypeUnsupportedRecord.hasType()) {
				throw new NdefEncoderException("Expected type", record);
			}

			domainAndType = externalTypeUnsupportedRecord.getDomain().trim().toLowerCase(Locale.US) + ":" + externalTypeUnsupportedRecord.getType().trim().toLowerCase(Locale.US);
			
			payload = externalTypeUnsupportedRecord.getData();
		} else {
			throw new NdefEncoderException("Unable to encode external type " + externalType.getClass().getName(), record);
		}
		
		byte[] type = domainAndType.getBytes(NdefConstants.UTF_8_CHARSET);
		return new NdefRecord(NdefConstants.TNF_EXTERNAL_TYPE, type, record.getId(), payload);
	}
	
	public void addRecordConfig(ExternalTypeRecordConfig config) {
		externalRecordTypes.put(config.getRecordClass(), config);
	}
}

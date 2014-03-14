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
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class ExternalTypeDecoder extends AbstractRecordDecoder<ExternalTypeRecord> {

	private Map<String, ExternalTypeRecordConfig> recordDecoders = new HashMap<String, ExternalTypeRecordConfig>();

	public ExternalTypeDecoder() {
		super(NdefConstants.TNF_EXTERNAL_TYPE);
	}

	@Override
	protected ExternalTypeRecord createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		String domainType = new String(ndefRecord.getType(), NdefConstants.DEFAULT_CHARSET);
		
		ExternalTypeRecordConfig config = recordDecoders.get(domainType);
		if(config != null) {
			return config.getContentDecoder().decodeContent(ndefRecord.getPayload());
		} else {
            int colon = domainType.lastIndexOf(':');

            String type;
            String domain;
            if(colon == -1) {
            	domain = domainType;
            	type = null;
            } else {
            	domain = domainType.substring(0, colon);
            	if(colon + 1 < domainType.length()) {
            		type = domainType.substring(colon + 1);
            	} else {
            		type = "";
            	}
            }
			
			// fall back to unsupported type
            return new GenericExternalTypeRecord(domain, type, ndefRecord.getPayload());
		}
	}

	public void addRecordConfig(ExternalTypeRecordConfig config) {
		recordDecoders.put(config.getNamespace(), config);
	}
}

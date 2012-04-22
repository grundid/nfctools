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

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractRecordDecoder;

public class ExternalTypeDecoder extends AbstractRecordDecoder<ExternalTypeRecord> {

	public ExternalTypeDecoder() {
		super(NdefConstants.TNF_EXTERNAL_TYPE);
	}

	@Override
	protected ExternalTypeRecord createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		String namespace = new String(ndefRecord.getType(), NdefConstants.DEFAULT_CHARSET);
		String content = new String(ndefRecord.getPayload(), NdefConstants.DEFAULT_CHARSET);
		ExternalTypeRecord record = null;
		if (NdefContext.getKnownExternalTypesByNamespace().containsKey(namespace)) {
			record = createKnownExternalType(namespace, content);
		}
		else
			record = new ExternalTypeRecord(namespace, content);
		return record;
	}

	private ExternalTypeRecord createKnownExternalType(String namespace, String content) {

		try {
			Class<? extends ExternalTypeRecord> recordClass = NdefContext.getKnownExternalTypesByNamespace().get(
					namespace);
			ExternalTypeRecord record = recordClass.newInstance();
			record.setContent(content);
			record.setNamespace(namespace);
			return record;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

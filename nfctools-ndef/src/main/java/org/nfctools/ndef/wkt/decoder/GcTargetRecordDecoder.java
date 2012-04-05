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
package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GcTargetRecord;

public class GcTargetRecordDecoder extends AbstractTypeRecordDecoder<GcTargetRecord> {

	public GcTargetRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN, GcTargetRecord.TYPE);
	}

	@Override
	public GcTargetRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		Record record = messageDecoder.decodeToRecord(ndefRecord.getPayload());
		GcTargetRecord targetRecord = new GcTargetRecord(record);
		setIdOnRecord(ndefRecord, targetRecord);
		return targetRecord;
	}

}

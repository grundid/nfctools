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

package org.nfctools.ndef.wkt.decoder.handover;

import java.util.List;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.decoder.AbstractTypeRecordDecoder;
import org.nfctools.ndef.wkt.records.handover.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.records.handover.CollisionResolutionRecord;
import org.nfctools.ndef.wkt.records.handover.HandoverRequestRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 *
 */

public class HandoverRequestRecordDecoder extends AbstractTypeRecordDecoder<HandoverRequestRecord> {

	public HandoverRequestRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN, HandoverRequestRecord.TYPE);
	}
	
	@Override
	protected HandoverRequestRecord createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		HandoverRequestRecord handoverRequestRecord = new HandoverRequestRecord();
		
		
		byte[] payload = ndefRecord.getPayload();
		
		byte minorVersion = (byte)(payload[0] & 0x0F);
		byte majorVersion = (byte)((payload[0] >> 4) & 0x0F);
		
		handoverRequestRecord.setMinorVersion(minorVersion);
		handoverRequestRecord.setMajorVersion(majorVersion);

		List<Record> records = messageDecoder.decodeToRecords(payload, 1, payload.length - 1);

		if(records.isEmpty()) {
			throw new IllegalArgumentException("Expected collision resolution record and at least one alternative carrier");
		}
		Record firstRecord = records.get(0);
		
		if(firstRecord instanceof CollisionResolutionRecord) {
			handoverRequestRecord.setCollisionResolution((CollisionResolutionRecord)firstRecord);
		} else {
			throw new IllegalArgumentException("Expected collision resolution record");
		}

		if(records.size() < 2) {
			throw new IllegalArgumentException("Expected at least one alternative carrier");
		}
		
		for(int i = 1; i < records.size(); i++) {
			handoverRequestRecord.add((AlternativeCarrierRecord)records.get(i));
		}

		return handoverRequestRecord;
	}

}

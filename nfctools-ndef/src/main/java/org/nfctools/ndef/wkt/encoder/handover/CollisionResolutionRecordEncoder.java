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

package org.nfctools.ndef.wkt.encoder.handover;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;
import org.nfctools.ndef.wkt.records.handover.CollisionResolutionRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 *
 */

public class CollisionResolutionRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof CollisionResolutionRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		
		CollisionResolutionRecord collisionResolutionRecord = (CollisionResolutionRecord)record;

		int randomNumber = collisionResolutionRecord.getRandomNumber();
		
		byte[] payload = new byte[]{(byte) ((randomNumber >> 8) & 0xFF), (byte)(randomNumber & 0xFF)}; // msb, lsb
		
		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, CollisionResolutionRecord.TYPE, record.getId(), payload);
	}

}

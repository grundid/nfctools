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
package org.nfctools.ndef;

import java.util.ArrayList;
import java.util.List;

import org.nfctools.ndef.wkt.encoder.RecordEncoder;

public class NdefRecordEncoder {

	public List<RecordEncoder> knownRecordEncoders = new ArrayList<RecordEncoder>();

	public NdefRecord encode(Record record, NdefMessageEncoder messageEncoder) {

		for (RecordEncoder encoder : knownRecordEncoders) {
			if (encoder.canEncode(record)) {
				return encoder.encodeRecord(record, messageEncoder);
			}
		}
		throw new RuntimeException("unsupported record [" + record.getClass().getName() + "]");
	}

	public void addRecordEncoder(RecordEncoder recordEncoder) {
		knownRecordEncoders.add(recordEncoder);
	}

}

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

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;

public abstract class AbstractRecordDecoder<T extends Record> implements RecordDecoder<T> {

	private int tnf;

	protected AbstractRecordDecoder(int tnf) {
		this.tnf = tnf;
	}

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		return ndefRecord.getTnf() == tnf;
	}

	protected abstract T createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder);

	@Override
	public T decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		T record = createRecord(ndefRecord, messageDecoder);
		record.setId(ndefRecord.getId());
		return record;
	}

}

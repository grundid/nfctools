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
package org.nfctools.mf.ndef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nfctools.mf.tlv.NdefMessageTlv;
import org.nfctools.mf.tlv.Tlv;
import org.nfctools.mf.tlv.TypeLengthValueReader;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessage;
import org.nfctools.ndef.NdefDecoder;
import org.nfctools.ndef.NdefOperations;
import org.nfctools.ndef.Record;

public abstract class AbstractNdefOperations implements NdefOperations {

	protected boolean formatted;
	protected boolean writable;
	protected List<Record> lastReadRecords;

	protected AbstractNdefOperations(boolean formatted, boolean writable) {
		this.formatted = formatted;
		this.writable = writable;
	}

	@Override
	public boolean hasNdefMessage() {
		if (lastReadRecords != null && !lastReadRecords.isEmpty())
			return true;

		Collection<Record> ndefMessage = readNdefMessage();
		return !ndefMessage.isEmpty();
	}

	@Override
	public boolean isFormatted() {
		return formatted;
	}

	@Override
	public boolean isWritable() {
		return writable;
	}

	@Override
	public void format() {
		format(new Record[0]);
	}

	@Override
	public void formatReadOnly(Record... records) {
		format(records);
		makeReadOnly();
	}

	protected void assertWritable() {
		if (!writable)
			throw new IllegalStateException("Tag not writable");
	}

	protected void assertFormatted() {
		if (!formatted)
			throw new IllegalStateException("Tag not formatted");
	}

	protected byte[] convertRecordsToBytes(Record[] records) {
		if (records.length == 0)
			return new byte[0];
		else {
			return NdefContext.getNdefEncoder().encode(records);
		}
	}

	protected void convertRecords(TypeLengthValueReader reader) {
		lastReadRecords = new ArrayList<Record>();

		NdefDecoder ndefMessageDecoder = NdefContext.getNdefDecoder();
		while (reader.hasNext()) {
			Tlv tlv = reader.next();
			if (tlv instanceof NdefMessageTlv) {
				
				byte[] payload = ((NdefMessageTlv)tlv).getNdefMessage();
				
				if(payload.length > 0) {
					List<Record> records = ndefMessageDecoder.decodeToRecords(payload);
					for (Record record : records) {
						lastReadRecords.add(record);
					}
				}
			}
		}
	}

}

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

package org.nfctools.ndef;

import java.util.ArrayList;
import java.util.List;

import org.nfctools.ndef.wkt.decoder.RecordDecoder;
import org.nfctools.ndef.wkt.records.UriRecord;

public class NdefRecordDecoder {

	public List<RecordDecoder<? extends Record>> knownRecordDecoders = new ArrayList<RecordDecoder<? extends Record>>();

	public Record decode(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		switch (ndefRecord.getTnf()) {
			case NdefConstants.TNF_EMPTY:
				break;
			case NdefConstants.TNF_WELL_KNOWN:
			case NdefConstants.TNF_EXTERNAL_TYPE:
				return handleWellKnownRecordType(ndefRecord, messageDecoder);
			case NdefConstants.TNF_MIME_MEDIA:
				return handleMimeMediaType(ndefRecord, messageDecoder);
			case NdefConstants.TNF_ABSOLUTE_URI:
				return handleAbsoluteUri(ndefRecord);
		}

		throw new RuntimeException("unsupported NDEF Type Name Format [" + ndefRecord.getTnf() + "]");
	}

	private UriRecord handleAbsoluteUri(NdefRecord ndefRecord) {
		return new UriRecord(new String(ndefRecord.getType()));
	}

	private Record handleMimeMediaType(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		for (RecordDecoder<? extends Record> decoder : knownRecordDecoders) {
			if (decoder.canDecode(ndefRecord)) {
				return decoder.decodeRecord(ndefRecord, messageDecoder);
			}
		}
		throw new RuntimeException("unsupported NDEF record type [" + new String(ndefRecord.getType()) + "]");
	}

	private Record handleWellKnownRecordType(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		for (RecordDecoder<? extends Record> decoder : knownRecordDecoders) {
			if (decoder.canDecode(ndefRecord)) {
				return decoder.decodeRecord(ndefRecord, messageDecoder);
			}
		}
		throw new RuntimeException("unsupported NDEF record type [" + new String(ndefRecord.getType()) + "]");
	}

	public void addRecordDecoder(RecordDecoder<? extends Record> recordDecoder) {
		knownRecordDecoders.add(recordDecoder);
	}
}

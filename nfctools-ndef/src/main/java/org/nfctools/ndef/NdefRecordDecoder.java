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

import org.nfctools.ndef.auri.AbsoluteUriRecordDecoder;
import org.nfctools.ndef.empty.EmptyRecordDecoder;
import org.nfctools.ndef.ext.ExternalTypeDecoder;
import org.nfctools.ndef.ext.ExternalTypeRecordConfig;
import org.nfctools.ndef.mime.MimeRecordDecoder;
import org.nfctools.ndef.unknown.UnknownRecordDecoder;
import org.nfctools.ndef.unknown.unsupported.UnsupportedRecord;
import org.nfctools.ndef.wkt.WellKnownRecordConfig;
import org.nfctools.ndef.wkt.WellKnownRecordDecoder;
import org.nfctools.ndef.wkt.decoder.RecordDecoder;

public class NdefRecordDecoder {

	private WellKnownRecordDecoder wellKnownRecordDecoder = new WellKnownRecordDecoder();

	private ExternalTypeDecoder externalTypeDecoder = new ExternalTypeDecoder();
	
	private List<RecordDecoder<? extends Record>> recordDecoders = new ArrayList<RecordDecoder<? extends Record>>();
	
	public NdefRecordDecoder() {
		recordDecoders.add(wellKnownRecordDecoder);
		recordDecoders.add(new AbsoluteUriRecordDecoder());
		recordDecoders.add(new MimeRecordDecoder());
		recordDecoders.add(externalTypeDecoder);
		recordDecoders.add(new EmptyRecordDecoder());
		recordDecoders.add(new UnknownRecordDecoder());
	}

	public Record decode(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		if (ndefRecord.isChunked()) {
			throw new IllegalArgumentException("Cannot decode chunked record");
		}

		for (RecordDecoder<? extends Record> decoder : recordDecoders) {
			if (decoder.canDecode(ndefRecord))
				return decoder.decodeRecord(ndefRecord, messageDecoder);
		}
		
		// NFC Data Exchange Format (NDEF) 1.0:
		// An NDEF parser that receives an NDEF record with an unknown or unsupported TNF field value SHOULD treat it as 0x05 (Unknown).
		// It is RECOMMENDED that an NDEF parser receiving an NDEF record of this type, 
		// without further context to its use, provides a mechanism for storing but not processing the payload.
		
		return new UnsupportedRecord(ndefRecord);
	}

	public void registerWellKnownRecordConfig(WellKnownRecordConfig recordconfig) {
		wellKnownRecordDecoder.addRecordConfig(recordconfig);
	}

	public void registerExternalTypeRecordConfig(ExternalTypeRecordConfig recordconfig) {
		externalTypeDecoder.addRecordConfig(recordconfig);
	}

	
}

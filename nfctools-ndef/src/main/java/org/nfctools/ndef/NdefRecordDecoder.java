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
import org.nfctools.ndef.mime.MimeRecordDecoder;
import org.nfctools.ndef.reserved.ReservedRecordDecoder;
import org.nfctools.ndef.unchanged.UnchangedRecordDecoder;
import org.nfctools.ndef.unknown.UnknownRecordDecoder;
import org.nfctools.ndef.wkt.decoder.RecordDecoder;

public class NdefRecordDecoder {

	// non-pluggable decoders
	private EmptyRecordDecoder emptyRecordDecoder = new EmptyRecordDecoder();
	private AbsoluteUriRecordDecoder absoluteUriRecordDecoder = new AbsoluteUriRecordDecoder();
	private MimeRecordDecoder mimeRecordDecoder = new MimeRecordDecoder();
	private UnchangedRecordDecoder unchangedRecordDecoder = new UnchangedRecordDecoder();
	private UnknownRecordDecoder unknownRecordDecoder = new UnknownRecordDecoder();
	private ReservedRecordDecoder reservedRecordDecoder = new ReservedRecordDecoder();
	
	// pluggable decoders
	// plug decoders which only look at the raw byte types
	/** well-known decoder types */
	private List<RecordDecoder<? extends Record>> wellKnownRecordDecoders = new ArrayList<RecordDecoder<? extends Record>>();
	/** external type decoders */
	private List<RecordDecoder<? extends Record>> externalRecordDecoders = new ArrayList<RecordDecoder<? extends Record>>();

	public Record decode(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {

		switch (ndefRecord.getTnf()) {
			case NdefConstants.TNF_EMPTY: {
				if(emptyRecordDecoder.canDecode(ndefRecord)) {
					return emptyRecordDecoder.decodeRecord(ndefRecord, messageDecoder);
				}
				break;
			}
			
			case NdefConstants.TNF_WELL_KNOWN:
				return handleWellKnownRecordType(ndefRecord, messageDecoder);
				
			case NdefConstants.TNF_MIME_MEDIA: {
				if(mimeRecordDecoder.canDecode(ndefRecord)) {
					return handleMimeMediaType(ndefRecord, messageDecoder);
				}
				break;
			}
			
			case NdefConstants.TNF_ABSOLUTE_URI: {
				if(absoluteUriRecordDecoder.canDecode(ndefRecord)) {
					return absoluteUriRecordDecoder.decodeRecord(ndefRecord, messageDecoder);
				}
				break;
			}
			
			case NdefConstants.TNF_EXTERNAL_TYPE:
				return handleExternalRecordType(ndefRecord, messageDecoder);
			case NdefConstants.TNF_UNKNOWN: {
				if(unknownRecordDecoder.canDecode(ndefRecord)) {
					return unknownRecordDecoder.decodeRecord(ndefRecord, messageDecoder);
				}
				break;
			}
			
			case NdefConstants.TNF_UNCHANGED: {
				if(unchangedRecordDecoder.canDecode(ndefRecord)) {
					return unchangedRecordDecoder.decodeRecord(ndefRecord, messageDecoder);
				}
				break;
			}
				
			case NdefConstants.TNF_RESERVED:
			{
				if(reservedRecordDecoder.canDecode(ndefRecord)) {
					return reservedRecordDecoder.decodeRecord(ndefRecord, messageDecoder);
				}
				break;
			}
		}

		throw new IllegalArgumentException("Unsupported NDEF Type Name Format [" + ndefRecord.getTnf() + "]");
	}

	private Record handleExternalRecordType(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		for (RecordDecoder<? extends Record> decoder : externalRecordDecoders) {
			if (decoder.canDecode(ndefRecord)) {
				return decoder.decodeRecord(ndefRecord, messageDecoder);
			}
		}
		throw new IllegalArgumentException("Unsupported NDEF record type [" + new String(ndefRecord.getType()) + "]");
	}

	private Record handleMimeMediaType(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		for (RecordDecoder<? extends Record> decoder : wellKnownRecordDecoders) {
			if (decoder.canDecode(ndefRecord)) {
				return decoder.decodeRecord(ndefRecord, messageDecoder);
			}
		}
		throw new IllegalArgumentException("Unsupported NDEF record type [" + new String(ndefRecord.getType()) + "]");
	}

	private Record handleWellKnownRecordType(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		for (RecordDecoder<? extends Record> decoder : wellKnownRecordDecoders) {
			if (decoder.canDecode(ndefRecord)) {
				return decoder.decodeRecord(ndefRecord, messageDecoder);
			}
		}
		throw new IllegalArgumentException("Unsupported NDEF record type [" + new String(ndefRecord.getType()) + "]");
	}

	public void addWellKnownRecordDecoder(RecordDecoder<? extends Record> recordDecoder) {
		wellKnownRecordDecoders.add(recordDecoder);
	}
	
	public void addExternalRecordDecoder(RecordDecoder<? extends Record> recordDecoder) {
		externalRecordDecoders.add(recordDecoder);
	}

}

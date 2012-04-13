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

import java.util.HashMap;
import java.util.Map;

import org.nfctools.ndef.ext.AndroidApplicationRecord;
import org.nfctools.ndef.ext.ExternalTypeRecord;
import org.nfctools.ndef.wkt.WellKnownRecordConfig;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.decoder.ActionRecordDecoder;
import org.nfctools.ndef.wkt.encoder.ActionRecordEncoder;
import org.nfctools.ndef.wkt.records.ActionRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * Simple entry class for getting the different encoders and decoders.
 * 
 */
public class NdefContext {

	private static NdefRecordEncoder ndefRecordEncoder = new NdefRecordEncoder();
	private static NdefRecordDecoder ndefRecordDecoder = new NdefRecordDecoder();
	private static NdefMessageEncoder ndefMessageEncoder = new NdefMessageEncoder(ndefRecordEncoder);
	private static NdefMessageDecoder ndefMessageDecoder = new NdefMessageDecoder(ndefRecordDecoder);

	private static final Map<String, Class<? extends ExternalTypeRecord>> knownExternalTypesByNamespace = new HashMap<String, Class<? extends ExternalTypeRecord>>();

	static {
		registerWellKnownRecord(new RecordType("act"), ActionRecord.class, new ActionRecordEncoder(),
				new ActionRecordDecoder());

		// decoders
		// well known decoders
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new SmartPosterDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new TextRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new UriRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new ActionRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new GenericControlRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new GcTargetRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new GcActionRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new GcDataRecordDecoder());
		//
		//		// handover
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new AlternativeCarrierRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverCarrierRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverRequestRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverSelectRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new ErrorRecordDecoder());
		//		ndefRecordDecoder.addWellKnownRecordDecoder(new CollisionResolutionRecordDecoder());

		// encoders

		//		// well known encoders
		//		ndefRecordEncoder.addRecordEncoder(new SmartPosterRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new TextRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new UriRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new ActionRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new GenericControlRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new GcTargetRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new GcActionRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new GcDataRecordEncoder());
		//
		//		// handover
		//		ndefRecordEncoder.addRecordEncoder(new AlternativeCarrierRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new HandoverCarrierRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new HandoverRequestRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new HandoverSelectRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new ErrorRecordEncoder());
		//		ndefRecordEncoder.addRecordEncoder(new CollisionResolutionRecordEncoder());

		// other decoders

		// Known External Type Records
		knownExternalTypesByNamespace.put(AndroidApplicationRecord.TYPE, AndroidApplicationRecord.class);

	}

	private static void registerWellKnownRecord(RecordType recordType, Class<? extends WellKnownRecord> recordClass,
			WellKnownRecordPayloadEncoder payloadEncoder, WellKnownRecordPayloadDecoder payloadDecoder) {
		WellKnownRecordConfig config = new WellKnownRecordConfig(recordType, recordClass, payloadEncoder,
				payloadDecoder);
		ndefRecordDecoder.registerRecordConfig(config);
		ndefRecordEncoder.registerRecordConfig(config);
	}

	public static NdefRecordDecoder getNdefRecordDecoder() {
		return ndefRecordDecoder;
	}

	public static NdefRecordEncoder getNdefRecordEncoder() {
		return ndefRecordEncoder;
	}

	public static NdefMessageDecoder getNdefMessageDecoder() {
		return ndefMessageDecoder;
	}

	public static NdefMessageEncoder getNdefMessageEncoder() {
		return ndefMessageEncoder;
	}

	public static Map<String, Class<? extends ExternalTypeRecord>> getKnownExternalTypesByNamespace() {
		return knownExternalTypesByNamespace;
	}
}

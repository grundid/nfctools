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
import org.nfctools.ndef.wkt.decoder.GcActionRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GcDataRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GcTargetRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GenericControlRecordDecoder;
import org.nfctools.ndef.wkt.decoder.SmartPosterRecordDecoder;
import org.nfctools.ndef.wkt.decoder.TextRecordDecoder;
import org.nfctools.ndef.wkt.decoder.UriRecordDecoder;
import org.nfctools.ndef.wkt.decoder.handover.AlternativeCarrierRecordDecoder;
import org.nfctools.ndef.wkt.decoder.handover.CollisionResolutionRecordDecoder;
import org.nfctools.ndef.wkt.decoder.handover.ErrorRecordDecoder;
import org.nfctools.ndef.wkt.decoder.handover.HandoverCarrierRecordDecoder;
import org.nfctools.ndef.wkt.decoder.handover.HandoverRequestRecordDecoder;
import org.nfctools.ndef.wkt.decoder.handover.HandoverSelectRecordDecoder;
import org.nfctools.ndef.wkt.encoder.ActionRecordEncoder;
import org.nfctools.ndef.wkt.encoder.GcActionRecordEncoder;
import org.nfctools.ndef.wkt.encoder.GcDataRecordEncoder;
import org.nfctools.ndef.wkt.encoder.GcTargetRecordEncoder;
import org.nfctools.ndef.wkt.encoder.GenericControlRecordEncoder;
import org.nfctools.ndef.wkt.encoder.SmartPosterRecordEncoder;
import org.nfctools.ndef.wkt.encoder.TextRecordEncoder;
import org.nfctools.ndef.wkt.encoder.UriRecordEncoder;
import org.nfctools.ndef.wkt.encoder.handover.AlternativeCarrierRecordEncoder;
import org.nfctools.ndef.wkt.encoder.handover.CollisionResolutionRecordEncoder;
import org.nfctools.ndef.wkt.encoder.handover.ErrorRecordEncoder;
import org.nfctools.ndef.wkt.encoder.handover.HandoverCarrierRecordEncoder;
import org.nfctools.ndef.wkt.encoder.handover.HandoverRequestRecordEncoder;
import org.nfctools.ndef.wkt.encoder.handover.HandoverSelectRecordEncoder;
import org.nfctools.ndef.wkt.records.ActionRecord;
import org.nfctools.ndef.wkt.records.GcActionRecord;
import org.nfctools.ndef.wkt.records.GcDataRecord;
import org.nfctools.ndef.wkt.records.GcTargetRecord;
import org.nfctools.ndef.wkt.records.GenericControlRecord;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;
import org.nfctools.ndef.wkt.records.handover.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.records.handover.CollisionResolutionRecord;
import org.nfctools.ndef.wkt.records.handover.ErrorRecord;
import org.nfctools.ndef.wkt.records.handover.HandoverCarrierRecord;
import org.nfctools.ndef.wkt.records.handover.HandoverRequestRecord;
import org.nfctools.ndef.wkt.records.handover.HandoverSelectRecord;

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
		registerWellKnownRecord(new RecordType("U"), UriRecord.class, new UriRecordEncoder(), new UriRecordDecoder());
		registerWellKnownRecord(new RecordType("T"), TextRecord.class, new TextRecordEncoder(), new TextRecordDecoder());
		registerWellKnownRecord(new RecordType("Sp"), SmartPosterRecord.class, new SmartPosterRecordEncoder(),
				new SmartPosterRecordDecoder());
		registerWellKnownRecord(new RecordType("Gc"), GenericControlRecord.class, new GenericControlRecordEncoder(),
				new GenericControlRecordDecoder());
		registerWellKnownRecord(new RecordType("t"), GcTargetRecord.class, new GcTargetRecordEncoder(),
				new GcTargetRecordDecoder());
		registerWellKnownRecord(new RecordType("d"), GcDataRecord.class, new GcDataRecordEncoder(),
				new GcDataRecordDecoder());
		registerWellKnownRecord(new RecordType("a"), GcActionRecord.class, new GcActionRecordEncoder(),
				new GcActionRecordDecoder());

		registerWellKnownRecord(new RecordType("ac"), AlternativeCarrierRecord.class,
				new AlternativeCarrierRecordEncoder(), new AlternativeCarrierRecordDecoder());
		registerWellKnownRecord(new RecordType("Hc"), HandoverCarrierRecord.class, new HandoverCarrierRecordEncoder(),
				new HandoverCarrierRecordDecoder());
		registerWellKnownRecord(new RecordType("Hr"), HandoverRequestRecord.class, new HandoverRequestRecordEncoder(),
				new HandoverRequestRecordDecoder());
		registerWellKnownRecord(new RecordType("Hs"), HandoverSelectRecord.class, new HandoverSelectRecordEncoder(),
				new HandoverSelectRecordDecoder());
		registerWellKnownRecord(new RecordType("err"), ErrorRecord.class, new ErrorRecordEncoder(),
				new ErrorRecordDecoder());
		registerWellKnownRecord(new RecordType("cr"), CollisionResolutionRecord.class,
				new CollisionResolutionRecordEncoder(), new CollisionResolutionRecordDecoder());

		// Known External Type Records
		knownExternalTypesByNamespace.put(AndroidApplicationRecord.TYPE, AndroidApplicationRecord.class);

	}

	public static void registerWellKnownRecord(RecordType recordType, Class<? extends WellKnownRecord> recordClass,
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

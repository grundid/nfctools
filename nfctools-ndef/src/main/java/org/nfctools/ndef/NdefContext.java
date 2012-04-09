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

import org.nfctools.ndef.auri.AbsoluteUriRecordEncoder;
import org.nfctools.ndef.empty.EmptyRecordEncoder;
import org.nfctools.ndef.ext.AndroidApplicationRecord;
import org.nfctools.ndef.ext.ExternalTypeDecoder;
import org.nfctools.ndef.ext.ExternalTypeEncoder;
import org.nfctools.ndef.ext.ExternalTypeRecord;
import org.nfctools.ndef.mime.MimeRecordEncoder;
import org.nfctools.ndef.unknown.UnknownRecordEncoder;
import org.nfctools.ndef.wkt.decoder.ActionRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GcActionRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GcDataRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GcTargetRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GenericControlRecordDecoder;
import org.nfctools.ndef.wkt.decoder.SmartPosterDecoder;
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
import org.nfctools.ndef.wkt.records.WellKnownRecord;
import org.nfctools.ndef.wkt.records.handover.AlternativeCarrierRecord;
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

	private static final Map<String, Class<? extends WellKnownRecord>> knownRecordsByType = new HashMap<String, Class<? extends WellKnownRecord>>();
	private static final Map<String, Class<? extends ExternalTypeRecord>> knownExternalTypesByNamespace = new HashMap<String, Class<? extends ExternalTypeRecord>>();

	static {
		// decoders
		// well known decoders
		ndefRecordDecoder.addWellKnownRecordDecoder(new SmartPosterDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new TextRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new UriRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new ActionRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new GenericControlRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new GcTargetRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new GcActionRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new GcDataRecordDecoder());

		// handover
		ndefRecordDecoder.addWellKnownRecordDecoder(new AlternativeCarrierRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverCarrierRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverRequestRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverSelectRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new ErrorRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new CollisionResolutionRecordDecoder());

		// external type decoders
		ndefRecordDecoder.addExternalRecordDecoder(new ExternalTypeDecoder()); // catch all external type

		// encoders
		ndefRecordEncoder.addRecordEncoder(new EmptyRecordEncoder());

		// well known encoders
		ndefRecordEncoder.addRecordEncoder(new SmartPosterRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new TextRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new UriRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new ActionRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new GenericControlRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new GcTargetRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new GcActionRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new GcDataRecordEncoder());

		// handover
		ndefRecordEncoder.addRecordEncoder(new AlternativeCarrierRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new HandoverCarrierRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new HandoverRequestRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new HandoverSelectRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new ErrorRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new CollisionResolutionRecordEncoder());

		// other decoders
		ndefRecordEncoder.addRecordEncoder(new MimeRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new AbsoluteUriRecordEncoder());

		ndefRecordEncoder.addRecordEncoder(new ExternalTypeEncoder());

		ndefRecordEncoder.addRecordEncoder(new UnknownRecordEncoder());

		// Known simple Well Known Records
		knownRecordsByType.put(new String(AlternativeCarrierRecord.TYPE), AlternativeCarrierRecord.class);
		knownRecordsByType.put(new String(HandoverCarrierRecord.TYPE), HandoverCarrierRecord.class);
		knownRecordsByType.put(new String(HandoverRequestRecord.TYPE), HandoverRequestRecord.class);
		knownRecordsByType.put(new String(HandoverSelectRecord.TYPE), HandoverSelectRecord.class);

		// Known External Type Records
		knownExternalTypesByNamespace.put(AndroidApplicationRecord.TYPE, AndroidApplicationRecord.class);
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

	public static Map<String, Class<? extends WellKnownRecord>> getKnownRecordsByType() {
		return knownRecordsByType;
	}

	public static Map<String, Class<? extends ExternalTypeRecord>> getKnownExternalTypesByNamespace() {
		return knownExternalTypesByNamespace;
	}
}

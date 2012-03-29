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

import org.nfctools.ndef.auri.AbsoluteUriRecordEncoder;
import org.nfctools.ndef.empty.EmptyRecordEncoder;
import org.nfctools.ndef.ext.AndroidApplicationRecordDecoder;
import org.nfctools.ndef.ext.AndroidApplicationRecordEncoder;
import org.nfctools.ndef.ext.ExternalTypeDecoder;
import org.nfctools.ndef.ext.ExternalTypeEncoder;
import org.nfctools.ndef.mime.MimeRecordEncoder;
import org.nfctools.ndef.reserved.ReservedRecordEncoder;
import org.nfctools.ndef.unchanged.UnchangedRecordEncoder;
import org.nfctools.ndef.unknown.UnknownRecordEncoder;
import org.nfctools.ndef.wkt.decoder.ActionRecordDecoder;
import org.nfctools.ndef.wkt.decoder.AlternativeCarrierRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GcActionRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GcDataRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GcTargetRecordDecoder;
import org.nfctools.ndef.wkt.decoder.GenericControlRecordDecoder;
import org.nfctools.ndef.wkt.decoder.HandoverCarrierRecordDecoder;
import org.nfctools.ndef.wkt.decoder.HandoverRequestRecordDecoder;
import org.nfctools.ndef.wkt.decoder.HandoverSelectRecordDecoder;
import org.nfctools.ndef.wkt.decoder.SmartPosterDecoder;
import org.nfctools.ndef.wkt.decoder.TextRecordDecoder;
import org.nfctools.ndef.wkt.decoder.UriRecordDecoder;
import org.nfctools.ndef.wkt.encoder.ActionRecordEncoder;
import org.nfctools.ndef.wkt.encoder.AlternativeCarrierRecordEncoder;
import org.nfctools.ndef.wkt.encoder.GcActionRecordEncoder;
import org.nfctools.ndef.wkt.encoder.GcDataRecordEncoder;
import org.nfctools.ndef.wkt.encoder.GcTargetRecordEncoder;
import org.nfctools.ndef.wkt.encoder.GenericControlRecordEncoder;
import org.nfctools.ndef.wkt.encoder.HandoverCarrierRecordEncoder;
import org.nfctools.ndef.wkt.encoder.HandoverRequestRecordEncoder;
import org.nfctools.ndef.wkt.encoder.HandoverSelectRecordEncoder;
import org.nfctools.ndef.wkt.encoder.SmartPosterRecordEncoder;
import org.nfctools.ndef.wkt.encoder.TextRecordEncoder;
import org.nfctools.ndef.wkt.encoder.UriRecordEncoder;

/**
 * Simple entry class for getting the different encoders and decoders.
 * 
 */
public class NdefContext {

	private static NdefRecordEncoder ndefRecordEncoder = new NdefRecordEncoder();
	private static NdefRecordDecoder ndefRecordDecoder = new NdefRecordDecoder();
	private static NdefMessageEncoder ndefMessageEncoder = new NdefMessageEncoder(ndefRecordEncoder);
	private static NdefMessageDecoder ndefMessageDecoder = new NdefMessageDecoder(ndefRecordDecoder);

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
		
		ndefRecordDecoder.addWellKnownRecordDecoder(new AlternativeCarrierRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverCarrierRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverRequestRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new HandoverSelectRecordDecoder());

		// external type decoders
		ndefRecordDecoder.addWellKnownRecordDecoder(new AndroidApplicationRecordDecoder());
		ndefRecordDecoder.addWellKnownRecordDecoder(new ExternalTypeDecoder()); // catch all external type

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

		ndefRecordEncoder.addRecordEncoder(new AlternativeCarrierRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new HandoverCarrierRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new HandoverRequestRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new HandoverSelectRecordEncoder());

		// other decoders
		ndefRecordEncoder.addRecordEncoder(new MimeRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new AbsoluteUriRecordEncoder());
		
		ndefRecordEncoder.addRecordEncoder(new AndroidApplicationRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new ExternalTypeEncoder()); // catch all external type
		
		ndefRecordEncoder.addRecordEncoder(new UnknownRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new UnchangedRecordEncoder());
		ndefRecordEncoder.addRecordEncoder(new ReservedRecordEncoder());


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
}

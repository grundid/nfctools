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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.nfctools.ndef.auri.AbsoluteUriRecord;
import org.nfctools.ndef.empty.EmptyRecord;
import org.nfctools.ndef.ext.AndroidApplicationRecord;
import org.nfctools.ndef.ext.GeoRecord;
import org.nfctools.ndef.mime.BinaryMimeRecord;
import org.nfctools.ndef.mime.TextMimeRecord;
import org.nfctools.ndef.unknown.UnknownRecord;
import org.nfctools.ndef.unknown.unsupported.UnsupportedRecord;
import org.nfctools.ndef.wkt.handover.records.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.handover.records.CollisionResolutionRecord;
import org.nfctools.ndef.wkt.handover.records.ErrorRecord;
import org.nfctools.ndef.wkt.handover.records.HandoverCarrierRecord;
import org.nfctools.ndef.wkt.handover.records.HandoverRequestRecord;
import org.nfctools.ndef.wkt.handover.records.HandoverSelectRecord;
import org.nfctools.ndef.wkt.handover.records.AlternativeCarrierRecord.CarrierPowerState;
import org.nfctools.ndef.wkt.handover.records.ErrorRecord.ErrorReason;
import org.nfctools.ndef.wkt.handover.records.HandoverCarrierRecord.CarrierTypeFormat;
import org.nfctools.ndef.wkt.records.Action;
import org.nfctools.ndef.wkt.records.ActionRecord;
import org.nfctools.ndef.wkt.records.SignatureRecord;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.ndef.wkt.records.SignatureRecord.CertificateFormat;
import org.nfctools.ndef.wkt.records.SignatureRecord.SignatureType;

/**
 * 
 * Record data binding encode/decode roundtrip test.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class NdefEncodeDecodeRoundtripTest {

	private static AbsoluteUriRecord absoluteUriRecord = new AbsoluteUriRecord("http://absolute.url");
	private static ActionRecord actionRecord = new ActionRecord(Action.SAVE_FOR_LATER);
	private static AndroidApplicationRecord androidApplicationRecord = new AndroidApplicationRecord("com.skjolberg.nfc");
	private static EmptyRecord emptyRecord = new EmptyRecord();
	private static TextMimeRecord textMimeRecord = new TextMimeRecord("text/xml; charset=utf-8", "abcd...���");
	private static BinaryMimeRecord binaryMimeRecord = new BinaryMimeRecord("application/binary",
			"<?xml version=\"1.0\" encoding=\"utf-8\"?><manifest xmlns:android=\"http://schemas.android.com/apk/res/android\" />"
					.getBytes());
	private static SmartPosterRecord smartPosterRecord = new SmartPosterRecord(new TextRecord("Title message",
			Charset.forName("UTF-8"), new Locale("no")), new UriRecord("http://smartposter.uri"), new ActionRecord(
			Action.OPEN_FOR_EDITING));
	private static TextRecord textRecord = new TextRecord("Text message", Charset.forName("UTF-8"), new Locale("no"));
	private static UnknownRecord unknownRecord = new UnknownRecord(new byte[]{0x00, 0x01, 0x02, 0x03});
	private static UriRecord uriRecord = new UriRecord("http://wellknown.url");
	
	private static CollisionResolutionRecord collisionResolutionRecord = new CollisionResolutionRecord((short)123);
	private static ErrorRecord errorRecord = new ErrorRecord(ErrorReason.PermanenteMemoryConstraints, new Long(321L));
	
	private static AlternativeCarrierRecord alternativeCarrierRecord = new AlternativeCarrierRecord(CarrierPowerState.Active, "http://blabla");
	private static HandoverSelectRecord handoverSelectRecord = new HandoverSelectRecord();
	private static HandoverCarrierRecord handoverCarrierRecord = new HandoverCarrierRecord(CarrierTypeFormat.AbsoluteURI, "http://absolute.url", new byte[]{0x00, 0x01, 0x02, 0x03});

	private static HandoverRequestRecord handoverRequestRecord = new HandoverRequestRecord(new CollisionResolutionRecord((short)321));

	private static SignatureRecord signatureRecord = new SignatureRecord(SignatureRecord.SignatureType.NOT_PRESENT, new byte[]{0x00, 0x01, 0x10, 0x11}, CertificateFormat.X_509, "http://certificate.uri");
	private static SignatureRecord signatureRecordMarker = new SignatureRecord(SignatureRecord.SignatureType.NOT_PRESENT);
	
	private static UnsupportedRecord unsupportedRecord = new UnsupportedRecord(NdefConstants.TNF_RESERVED, "abc".getBytes(), "id".getBytes(), "DEF".getBytes());
	private static GeoRecord addressInformationGeoRecord = new GeoRecord("Oslo");
	private static GeoRecord coordinatesGeoRecord = new GeoRecord(59.949444, 10.756389);
	private static GeoRecord coordinatesAltitudeGeoRecord = new GeoRecord(59.949444, 10.756389, 100.0);
	
	public static Record[] records = new Record[] { absoluteUriRecord, actionRecord, androidApplicationRecord,
			emptyRecord, textMimeRecord, binaryMimeRecord, smartPosterRecord, textRecord, unknownRecord, uriRecord,
			collisionResolutionRecord, errorRecord,
			alternativeCarrierRecord, handoverSelectRecord, handoverCarrierRecord, handoverRequestRecord,
			
			signatureRecordMarker, signatureRecord,
			
			unsupportedRecord,
			addressInformationGeoRecord, coordinatesGeoRecord, coordinatesAltitudeGeoRecord
			};


	static {
		// handover request record requires at least on alternative carrier record
		AlternativeCarrierRecord alternativeCarrierRecord = new AlternativeCarrierRecord(CarrierPowerState.Active, "z");
		alternativeCarrierRecord.addAuxiliaryDataReference("a");
		alternativeCarrierRecord.addAuxiliaryDataReference("b");
		handoverRequestRecord.add(alternativeCarrierRecord);
		
		alternativeCarrierRecord = new AlternativeCarrierRecord(CarrierPowerState.Active, "y");
		alternativeCarrierRecord.addAuxiliaryDataReference("c");
		alternativeCarrierRecord.addAuxiliaryDataReference("d");

		handoverRequestRecord.add(alternativeCarrierRecord);

		handoverSelectRecord.add(alternativeCarrierRecord);
		handoverSelectRecord.setError(new ErrorRecord(ErrorReason.PermanenteMemoryConstraints, new Long(1L)));
		
		// add some certificates to signature
		signatureRecord.add(new byte[]{0x00, 0x10, 0x11});
		signatureRecord.setSignatureType(SignatureType.RSASSA_PSS_SHA_1);
		signatureRecord.setSignature(new byte[]{0x01, 0x11, 0x12});
	}

	@Test
	public void testEncodeDecodeRoundtrip() {

		NdefMessageEncoder ndefMessageEncoder = NdefContext.getNdefMessageEncoder();

		NdefMessageDecoder ndefMessageDecoder = NdefContext.getNdefMessageDecoder();

		for (Record record : records) {
			byte[] ndef = ndefMessageEncoder.encodeSingle(record);

			Record decodedRecord = ndefMessageDecoder.decodeToRecord(ndef);

			if(!record.equals(decodedRecord)) {
				fail(record.getClass().getName());
			}
		}
		
		List<Record> decodeToRecords = ndefMessageDecoder.decodeToRecords(ndefMessageEncoder.encode(records));
		for(int i = 0; i < decodeToRecords.size(); i++) {
			if(!records[i].equals(decodeToRecords.get(i))) {
				fail(records[i].getClass().getName());
			}
		}
		
		// check byte level just to make sure
		byte[] encoded = ndefMessageEncoder.encode(records);
		byte[] encodedDecodedEncoded = ndefMessageEncoder.encode(ndefMessageDecoder.decodeToRecords(ndefMessageEncoder.encode(records)));
		
		assertEquals(encoded.length, encodedDecodedEncoded.length);
		for(int i = 0; i < encoded.length; i++) {
			assertEquals(Integer.toString(i), encoded[i], encodedDecodedEncoded[i]);
		}
		
	}
}

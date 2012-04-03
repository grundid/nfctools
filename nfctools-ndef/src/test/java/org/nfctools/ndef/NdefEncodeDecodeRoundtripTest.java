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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.nfctools.ndef.auri.AbsoluteUriRecord;
import org.nfctools.ndef.empty.EmptyRecord;
import org.nfctools.ndef.ext.AndroidApplicationRecord;
import org.nfctools.ndef.mime.BinaryMimeRecord;
import org.nfctools.ndef.mime.TextMimeRecord;
import org.nfctools.ndef.reserved.ReservedRecord;
import org.nfctools.ndef.unchanged.UnchangedRecord;
import org.nfctools.ndef.unknown.UnknownRecord;
import org.nfctools.ndef.wkt.records.Action;
import org.nfctools.ndef.wkt.records.ActionRecord;
import org.nfctools.ndef.wkt.records.AlternativeCarrierRecord;
import org.nfctools.ndef.wkt.records.HandoverCarrierRecord;
import org.nfctools.ndef.wkt.records.HandoverRequestRecord;
import org.nfctools.ndef.wkt.records.HandoverSelectRecord;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;

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
	private static TextMimeRecord textMimeRecord = new TextMimeRecord("text/xml; charset=utf-8", "abcd...רזו");
	private static BinaryMimeRecord binaryMimeRecord = new BinaryMimeRecord("application/binary", "<?xml version=\"1.0\" encoding=\"utf-8\"?><manifest xmlns:android=\"http://schemas.android.com/apk/res/android\" />".getBytes());
	private static SmartPosterRecord smartPosterRecord = new SmartPosterRecord(new TextRecord("Title message", Charset.forName("UTF-8"), new Locale("no")), new UriRecord("http://smartposter.uri"), new ActionRecord(Action.OPEN_FOR_EDITING));
	private static TextRecord textRecord = new TextRecord("Text message", Charset.forName("UTF-8"), new Locale("no"));
	private static UnknownRecord unknownRecord = new UnknownRecord();
	private static UriRecord uriRecord = new UriRecord("http://wellknown.url");
	private static AlternativeCarrierRecord alternativeCarrierRecord = new AlternativeCarrierRecord();
	private static HandoverSelectRecord handoverSelectRecord = new HandoverSelectRecord();
	private static HandoverCarrierRecord handoverCarrierRecord = new HandoverCarrierRecord();
	private static UnchangedRecord unchangedRecord = new UnchangedRecord();

	private static ReservedRecord reservedRecord = new ReservedRecord();
	private static HandoverRequestRecord handoverRequestRecord = new HandoverRequestRecord();

	public static Record[] records = new Record[]{
			absoluteUriRecord,
			actionRecord,
			androidApplicationRecord,
			emptyRecord,
			textMimeRecord,
			binaryMimeRecord,
			smartPosterRecord,
			textRecord,
			unknownRecord,
			uriRecord,
			alternativeCarrierRecord,
			handoverSelectRecord,
			handoverCarrierRecord,
			handoverRequestRecord,
			reservedRecord,
			unchangedRecord
	};
	
	@Test
	public void testEncodeDecodeRoundtrip() {

		NdefMessageEncoder ndefMessageEncoder = NdefContext.getNdefMessageEncoder();
			
		List<Record> originalRecords = new ArrayList<Record>();
		for(Record record : records) {
			originalRecords.add(record);
		}
		byte[] ndef = ndefMessageEncoder.encode(originalRecords);

		NdefMessageDecoder ndefMessageDecoder = NdefContext.getNdefMessageDecoder();
		NdefMessage decode = ndefMessageDecoder.decode(ndef);
		
		List<Record> roundTripRecordes = ndefMessageDecoder.decodeToRecords(decode);
		
		for(int i = 0; i < roundTripRecordes.size(); i++) {
			assertEquals(Integer.toString(i),  originalRecords.get(i), roundTripRecordes.get(i));
		}
		
	}	
}

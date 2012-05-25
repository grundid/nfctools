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
package org.nfctools.ndef.decoder;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.Action;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.utils.NfcUtils;

public class SmartPosterDecoderTest {

	public static final String smartPoster = "D10227537091010B540564652D44455469746C6511010D550177696E667574757"
			+ "2652E646551030161637400";

	@Test
	public void testDecode() throws Exception {
		Record record = NdefContext.getNdefMessageDecoder().decodeToRecord(NfcUtils.convertASCIIToBin(smartPoster));
		assertTrue(record instanceof SmartPosterRecord);
		SmartPosterRecord smartPosterRecord = (SmartPosterRecord)record;

		assertEquals("Title", smartPosterRecord.getTitle().getText());
		assertEquals(Action.DEFAULT_ACTION, smartPosterRecord.getAction().getAction());
		assertEquals("http://www.winfuture.de", smartPosterRecord.getUri().getUri());

	}
}

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
package org.nfctools.snep;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.test.NfcipHelper;
import org.nfctools.test.NotifyingNdefListener;

public class SnepServerTest {

	private NotifyingNdefListener ndefListener;
	private SnepClient snepClient;
	private SnepServer snepServer;
	private NfcipHelper helper;

	public SnepServerTest() {
		ndefListener = new NotifyingNdefListener(this);
		snepServer = new SnepServer(ndefListener);
		snepClient = new SnepClient();
		helper = new NfcipHelper(snepServer, snepClient, null, null);
	}

	@Test
	public void testSnepPut() throws Exception {
		snepClient.setSnepAgentListener(new SnepAgentListener() {

			@Override
			public void onSnepConnection(SnepAgent snepAgent) {
				List<Record> records = new ArrayList<Record>();
				for (int x = 0; x < 50; x++)
					records.add(new UriRecord("http://www.nfctools.org"));
				snepAgent.doPut(records, ndefListener);
			}

			@Override
			public boolean hasDataToSend() {
				return true;
			}
		});
		helper.launch();
		synchronized (this) {
			wait(500000);
		}
		assertTrue(ndefListener.isSuccess());
		Collection<Record> receivedRecords = ndefListener.getRecords();
		assertEquals(50, receivedRecords.size());
		UriRecord uriRecord = (UriRecord)receivedRecords.iterator().next();
		assertEquals("http://www.nfctools.org", uriRecord.getUri());
	}

	@Test
	public void testSnepGet() throws Exception {
		List<Record> records = new ArrayList<Record>();
		records.add(new UriRecord("http://www.nfctools.org"));
		ndefListener.setGetResponseRecords(records);
		snepClient.setSnepAgentListener(new SnepAgentListener() {

			@Override
			public void onSnepConnection(SnepAgent snepAgent) {
				List<Record> records = new ArrayList<Record>();
				for (int x = 0; x < 50; x++)
					records.add(new UriRecord("http://www.nfctools.org"));
				snepAgent.doGet(records, ndefListener);
			}

			@Override
			public boolean hasDataToSend() {
				return true;
			}
		});
		helper.launch();
		synchronized (this) {
			wait(500000);
		}
		Collection<Record> receivedRecords = ndefListener.getReceivedGetResponseRecords();
		assertEquals(1, receivedRecords.size());
		UriRecord uriRecord = (UriRecord)receivedRecords.iterator().next();
		assertEquals("http://www.nfctools.org", uriRecord.getUri());
	}
}

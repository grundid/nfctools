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
package org.nfctools.test;

import java.util.Collection;

import org.nfctools.ndef.NdefListener;
import org.nfctools.ndef.Record;
import org.nfctools.snep.GetResponseListener;
import org.nfctools.snep.PutResponseListener;
import org.nfctools.snep.SnepAgent;
import org.nfctools.snep.Sneplet;

public class NotifyingNdefListener implements NdefListener, Sneplet, GetResponseListener, PutResponseListener {

	private Object objectToNotify;
	private Collection<Record> records;
	private Collection<Record> getResponseRecords;
	private Collection<Record> receivedGetResponseRecords;

	private boolean success = false;
	private boolean failed = false;

	public NotifyingNdefListener(Object objectToNotify) {
		this.objectToNotify = objectToNotify;
	}

	@Override
	public void onNdefMessages(Collection<Record> records) {
		this.records = records;
		synchronized (objectToNotify) {
			objectToNotify.notify();
		}
	}

	public Collection<Record> getRecords() {
		return records;
	}

	public void setGetResponseRecords(Collection<Record> getResponseRecords) {
		this.getResponseRecords = getResponseRecords;
	}

	@Override
	public Collection<Record> doGet(Collection<Record> requestRecords) {
		return getResponseRecords;
	}

	@Override
	public void doPut(Collection<Record> requestRecords) {
		this.records = requestRecords;
	}

	@Override
	public void onGetResponse(Collection<Record> records, SnepAgent snepAgent) {
		receivedGetResponseRecords = records;
		synchronized (objectToNotify) {
			objectToNotify.notify();
		}
	}

	public Collection<Record> getReceivedGetResponseRecords() {
		return receivedGetResponseRecords;
	}

	@Override
	public void onFailed() {
		failed = true;
	}

	@Override
	public void onSuccess() {
		success = true;
		synchronized (objectToNotify) {
			objectToNotify.notify();
		}
	}

	public void resetStatus() {
		failed = false;
		success = false;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isFailed() {
		return failed;
	}
}

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

import java.util.Collection;
import java.util.List;

import org.nfctools.ndef.Record;

public class SnepRequestContainer implements SnepAgent {

	private Collection<Record> records;
	private GetResponseListener getResponseListener;
	private PutResponseListener putResponseListener;
	private Request request;

	@Override
	public void doGet(Collection<Record> records, GetResponseListener getResponseListener) {
		this.records = records;
		this.getResponseListener = getResponseListener;
		request = Request.GET;
	}

	@Override
	public void doPut(Collection<Record> records, PutResponseListener putResponseListener) {
		this.records = records;
		this.putResponseListener = putResponseListener;
		request = Request.PUT;
	}

	public boolean hasRequest() {
		return request != null;
	}

	public Collection<Record> getRecords() {
		return records;
	}

	public Request getRequest() {
		return request;
	}

	public GetResponseListener getGetResponseListener() {
		return getResponseListener;
	}

	public PutResponseListener getPutResponseListener() {
		return putResponseListener;
	}

	public void handleSuccess(List<Record> receivedRecords) {
		Request oldRequest = request;
		request = null;
		if (oldRequest == Request.GET && getResponseListener != null) {
			getResponseListener.onGetResponse(receivedRecords, this);
		}
		else if (oldRequest == Request.PUT && putResponseListener != null) {
			putResponseListener.onSuccess();
		}
	}

	public void handleFailure() {
		Request oldRequest = request;
		request = null;
		if (oldRequest == Request.GET && getResponseListener != null) {
			getResponseListener.onFailed();
		}
		else if (oldRequest == Request.PUT && putResponseListener != null) {
			putResponseListener.onFailed();
		}
	}
}

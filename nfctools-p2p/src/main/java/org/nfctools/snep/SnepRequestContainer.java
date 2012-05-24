package org.nfctools.snep;

import java.util.Collection;

import org.nfctools.ndef.Record;

public class SnepRequestContainer implements SnepAgent {

	private Collection<Record> records;
	private GetResponseListener getResponseListener;
	private Request request;

	@Override
	public void doGet(Collection<Record> records, GetResponseListener getResponseListener) {
		this.records = records;
		this.getResponseListener = getResponseListener;
		request = Request.GET;
	}

	public boolean hasRequest() {
		return request != null;
	}

	@Override
	public void doPut(Collection<Record> records) {
		this.records = records;
		request = Request.PUT;
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

}

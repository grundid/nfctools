package org.nfctools.ndef.wkt.records;

import org.nfctools.ndef.Record;

public class GcActionRecord extends Record {

	public static final byte[] TYPE = { 'a' };
	public static final byte NUMERIC_CODE = 0x01;

	private Action action;
	private Record actionRecord;

	public GcActionRecord(Record actionRecord) {
		this.actionRecord = actionRecord;
	}

	public GcActionRecord(Action action) {
		this.action = action;
	}

	public boolean hasActionRecord() {
		return actionRecord != null;
	}

	public boolean hasAction() {
		return actionRecord == null;
	}

	public Action getAction() {
		return action;
	}

	public Record getActionRecord() {
		return actionRecord;
	}

	@Override
	public String toString() {
		return "Action: [" + (hasActionRecord() ? getActionRecord() : getAction()) + "]";
	}
}

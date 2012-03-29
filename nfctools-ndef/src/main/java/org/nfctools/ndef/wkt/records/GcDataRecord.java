package org.nfctools.ndef.wkt.records;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nfctools.ndef.Record;

public class GcDataRecord extends Record {

	public static final byte[] TYPE = { 'd' };

	private List<Record> records = new ArrayList<Record>();

	public GcDataRecord(Record... records) {
		for (Record record : records) {
			add(record);
		}
	}

	public GcDataRecord(Collection<? extends Record> records) {
		for (Record record : records) {
			add(record);
		}
	}

	public void add(Record record) {
		records.add(record);
	}

	public List<Record> getRecords() {
		return records;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Data: [");
		for (Record record : records) {
			sb.append(record).append(" ");
		}
		sb.append("]");
		return sb.toString();
	}

}

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
package org.nfctools.ndef.wkt.records;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nfctools.ndef.Record;

public class GcDataRecord extends WellKnownRecord {

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

	public void remove(Record record) {
		records.remove(record);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((records == null) ? 0 : records.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GcDataRecord other = (GcDataRecord)obj;
		if (records == null) {
			if (other.records != null)
				return false;
		}
		else if (!records.equals(other.records))
			return false;
		return true;
	}

}

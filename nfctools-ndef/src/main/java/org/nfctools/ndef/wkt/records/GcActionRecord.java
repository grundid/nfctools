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

import org.nfctools.ndef.Record;

public class GcActionRecord extends WellKnownRecord {

	public static final byte NUMERIC_CODE = 0x01;

	private Action action;
	private Record actionRecord;

	public GcActionRecord(Record actionRecord) {
		this.actionRecord = actionRecord;
	}

	public GcActionRecord(Action action) {
		this.action = action;
	}

	public GcActionRecord() {
	}

	public boolean hasActionRecord() {
		return actionRecord != null;
	}

	public boolean hasAction() {
		return action != null;
	}

	public Action getAction() {
		return action;
	}

	public void setActionRecord(Record actionRecord) {
		this.actionRecord = actionRecord;
	}

	public Record getActionRecord() {
		return actionRecord;
	}

	@Override
	public String toString() {
		return "Action: [" + (hasActionRecord() ? getActionRecord() : getAction()) + "]";
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((actionRecord == null) ? 0 : actionRecord.hashCode());
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
		GcActionRecord other = (GcActionRecord)obj;
		if (action != other.action)
			return false;
		if (actionRecord == null) {
			if (other.actionRecord != null)
				return false;
		}
		else if (!actionRecord.equals(other.actionRecord))
			return false;
		return true;
	}

}

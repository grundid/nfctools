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

public class GcTargetRecord extends WellKnownRecord {

	private Record targetIdentifier;

	public GcTargetRecord(Record targetIdentifier) {
		setTargetIdentifier(targetIdentifier);
	}

	public GcTargetRecord() {
	}

	public void setTargetIdentifier(Record targetIdentifier) {
		if (targetIdentifier != null) {
			if ((targetIdentifier instanceof UriRecord) || (targetIdentifier instanceof TextRecord))
				this.targetIdentifier = targetIdentifier;
			else
				throw new IllegalArgumentException(targetIdentifier.getClass().getName()
						+ " not supported as target identifier");
		}
		else {
			this.targetIdentifier = null;
		}
	}

	public Record getTargetIdentifier() {
		return targetIdentifier;
	}

	@Override
	public String toString() {
		return "Target: [" + targetIdentifier + "]";
	}

	public boolean hasTargetIdentifier() {
		return targetIdentifier != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((targetIdentifier == null) ? 0 : targetIdentifier.hashCode());
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
		GcTargetRecord other = (GcTargetRecord)obj;
		if (targetIdentifier == null) {
			if (other.targetIdentifier != null)
				return false;
		}
		else if (!targetIdentifier.equals(other.targetIdentifier))
			return false;
		return true;
	}

}

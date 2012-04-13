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

public class GenericControlRecord extends WellKnownRecord {

	private static final byte CB_CHECK_EXIT_CONDITION = 0x02;
	private static final byte CB_IGNORE_FOLLOWING_IF_FAILED = 0x04;

	private byte configurationByte;
	private GcTargetRecord target;
	private GcActionRecord action;
	private GcDataRecord data;

	public GenericControlRecord(GcTargetRecord target) {
		this.target = target;
	}

	public GenericControlRecord(GcTargetRecord target, byte configurationByte) {
		this.target = target;
		this.configurationByte = configurationByte;
	}

	public GenericControlRecord() {
	}

	public void setConfigurationByte(byte configurationByte) {
		this.configurationByte = configurationByte;
	}

	public byte getConfigurationByte() {
		return configurationByte;
	}

	public boolean isIgnoreFollowingIfFailed() {
		return (configurationByte & CB_IGNORE_FOLLOWING_IF_FAILED) != 0;
	}

	public void setIgnoreFollowingIfFailed() {
		configurationByte |= CB_IGNORE_FOLLOWING_IF_FAILED;
	}

	public boolean isCheckExitCondition() {
		return (configurationByte & CB_CHECK_EXIT_CONDITION) != 0;
	}

	public void setCheckExitCondition() {
		configurationByte |= CB_CHECK_EXIT_CONDITION;
	}

	public GcTargetRecord getTarget() {
		return target;
	}

	public void setTarget(GcTargetRecord target) {
		this.target = target;
	}

	public GcActionRecord getAction() {
		return action;
	}

	public void setAction(GcActionRecord action) {
		this.action = action;
	}

	public GcDataRecord getData() {
		return data;
	}

	public void setData(GcDataRecord data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GenericControl: [" + getTarget() + ", " + getAction() + ", " + getData() + "]";
	}

	public boolean hasTarget() {
		return target != null;
	}

	public boolean hasAction() {
		return action != null;
	}

	public boolean hasData() {
		return data != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + configurationByte;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		GenericControlRecord other = (GenericControlRecord)obj;
		if (action == null) {
			if (other.action != null)
				return false;
		}
		else if (!action.equals(other.action))
			return false;
		if (configurationByte != other.configurationByte)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		}
		else if (!data.equals(other.data))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		}
		else if (!target.equals(other.target))
			return false;
		return true;
	}

}

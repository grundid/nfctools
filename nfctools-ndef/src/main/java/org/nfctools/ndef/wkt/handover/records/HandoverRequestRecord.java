/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
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

package org.nfctools.ndef.wkt.handover.records;

import java.util.ArrayList;
import java.util.List;

import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * The Handover Request Record identifies a list of possible alternative carriers that the Handover Requester device
 * would be able to use for further communication with the Handover Selector. At least a single alternative carrier MUST
 * be specified by the Handover Requester. If multiple alternative carriers are specified, the Handover Selector SHOULD
 * process the records in order and acknowledge the first appropriate match, if any.
 * 
 * Only Alternative Carrier Records or Collision Resolution Records have a defined meaning in the payload of a Handover
 * Request Record. However, an implementation SHALL silently ignore and SHALL NOT raise an error if it encounters other
 * unknown record types.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverRequestRecord extends WellKnownRecord {

	/**
	 * This 4-bit field equals the major version number of the Connection Handover specification and SHALL be set to 0x1
	 * by an implementation that conforms to this specification. When an NDEF parser reads a different value, it SHALL
	 * NOT assume backward compatibility.
	 */
	private byte majorVersion = 0x01;

	/**
	 * This 4-bit field equals the minor version number of the Connection Handover specification and SHALL be set to 0x2
	 * by an implementation that conforms to this specification. When an NDEF parser reads a different value, it MAY
	 * assume backward compatibility.
	 */

	private byte minorVersion = 0x02;

	/**
	 * This record contains a 16-bit random number that is used in the collision resolution procedure defined in section
	 * 2.7. Only a single Collision Resolution Record SHALL be allowed in a Handover Request Message
	 */

	private CollisionResolutionRecord collisionResolution;

	/**
	 * Each record specifies a single alternative carrier that the Handover Requester would be able to utilize for
	 * further communication with the Handover Selector device.
	 */
	private List<AlternativeCarrierRecord> alternativeCarriers;

	public HandoverRequestRecord() {
		alternativeCarriers = new ArrayList<AlternativeCarrierRecord>();
	}

	public HandoverRequestRecord(CollisionResolutionRecord collisionResolution) {
		this(collisionResolution, new ArrayList<AlternativeCarrierRecord>());
	}

	public HandoverRequestRecord(CollisionResolutionRecord collisionResolution,
			List<AlternativeCarrierRecord> alternativeCarriers) {
		this.collisionResolution = collisionResolution;
		this.alternativeCarriers = alternativeCarriers;
	}

	public HandoverRequestRecord(byte majorVersion, byte minorVersion, CollisionResolutionRecord collisionResolution,
			List<AlternativeCarrierRecord> alternativeCarriers) {
		this(collisionResolution, alternativeCarriers);
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	public byte getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(byte majorVersion) {
		this.majorVersion = majorVersion;
	}

	public byte getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(byte minorVersion) {
		this.minorVersion = minorVersion;
	}

	public List<AlternativeCarrierRecord> getAlternativeCarriers() {
		return alternativeCarriers;
	}

	public void setAlternativeCarriers(List<AlternativeCarrierRecord> alternativeCarriers) {
		this.alternativeCarriers = alternativeCarriers;
	}

	public CollisionResolutionRecord getCollisionResolution() {
		return collisionResolution;
	}

	public void setCollisionResolution(CollisionResolutionRecord collisionResolution) {
		this.collisionResolution = collisionResolution;
	}

	public boolean hasAlternativeCarriers() {
		return !alternativeCarriers.isEmpty();
	}

	public void add(AlternativeCarrierRecord alternativeCarrierRecord) {
		this.alternativeCarriers.add(alternativeCarrierRecord);
	}

	public boolean hasCollisionResolution() {
		return this.collisionResolution != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alternativeCarriers == null) ? 0 : alternativeCarriers.hashCode());
		result = prime * result + ((collisionResolution == null) ? 0 : collisionResolution.hashCode());
		result = prime * result + majorVersion;
		result = prime * result + minorVersion;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HandoverRequestRecord other = (HandoverRequestRecord)obj;
		if (alternativeCarriers == null) {
			if (other.alternativeCarriers != null)
				return false;
		}
		else if (!alternativeCarriers.equals(other.alternativeCarriers))
			return false;
		if (collisionResolution == null) {
			if (other.collisionResolution != null)
				return false;
		}
		else if (!collisionResolution.equals(other.collisionResolution))
			return false;
		if (majorVersion != other.majorVersion)
			return false;
		if (minorVersion != other.minorVersion)
			return false;
		return true;
	}

}
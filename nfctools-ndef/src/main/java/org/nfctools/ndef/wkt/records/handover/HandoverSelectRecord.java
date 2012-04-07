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

package org.nfctools.ndef.wkt.records.handover;

import java.util.ArrayList;
import java.util.List;

import org.nfctools.ndef.wkt.records.AbstractWellKnownRecord;


/**
 * 
 * The Handover Select Record identifies the alternative carriers that the Handover Selector device
 * selected from the list provided within the previous Handover Request Message. The Handover
 * Selector MAY acknowledge zero, one, or more of the proposed alternative carriers at its own
 * discretion. 
 * 
 * Only Alternative Carrier Records have a defined meaning in the payload of a Handover Select
 * Record. However, an implementation SHALL NOT raise an error if it encounters other record
 * types, but SHOULD silently ignore them.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverSelectRecord extends AbstractWellKnownRecord {

	public static final byte[] TYPE = { 0x48, 0x73 }; // "Hs"

	/**
	 * This 4-bit field equals the major version number of the Connection
	 * Handover specification and SHALL be set to 0x1 by an implementation that conforms to this
	 * specification. When an NDEF parser reads a different value, it SHALL NOT assume backward
	 * compatibility.
	 * 
	 */
	
	private byte majorVersion = 0x01;
	
	/**
	 This 4-bit field equals the minor version number of the Connection
	* Handover specification and SHALL be set to 0x0 by an implementation that conforms to this
	* specification. When an NDEF parser reads a different value, it MAY assume backward
	* compatibility.
	*/
	private byte minorVersion = 0x00;
	
	/** Each record specifies a single alternative carrier that
	* the Handover Selector would be able to utilize for further communication with the Handover
	* Requester device. The order of the Alternative Carrier Records gives an implicit preference
	* ranking that the Handover Requester SHOULD obey.
	*/
	private List<AlternativeCarrierRecord> alternativeCarriers = new ArrayList<AlternativeCarrierRecord>();

	private ErrorRecord error;
	
	public HandoverSelectRecord() {
		super(TYPE);
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

	public void setAlternativeCarriers(
			List<AlternativeCarrierRecord> alternativeCarriers) {
		this.alternativeCarriers = alternativeCarriers;
	}

	public ErrorRecord getError() {
		return error;
	}

	public void setError(ErrorRecord error) {
		this.error = error;
	}

	

}
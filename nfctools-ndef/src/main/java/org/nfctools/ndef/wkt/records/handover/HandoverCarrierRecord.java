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

import org.nfctools.ndef.wkt.records.AbstractWellKnownRecord;

/**
 * 
 * The Handover Carrier Record provides a unique identification of an alternative carrier technology
 * in Handover Request messages when no carrier configuration data is to be provided. If the
 * Handover Selector has the same carrier technology available, it would respond with a Carrier
 * Configuration record with payload type equal to the carrier type (that is, the triples {TNF,
 * TYPE_LENGTH, TYPE} and {CTF, CARRIER_TYPE_LENGTH, CARRIER_TYPE} match
 * exactly).
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverCarrierRecord extends AbstractWellKnownRecord {

	public static final byte[] TYPE = { 0x48, 0x63 }; // "Hc"

	/** This is a 3-bit field that indicates the structure of the value of the CARRIER_TYPE field. */
	public static enum CarrierTypeFormat {
		
		WellKnown((byte)0x01), Media((byte)0x02), AbsoluteURI((byte)0x03), External((byte)0x04);
		
		private CarrierTypeFormat(byte value) {
			this.value = value;
		}
		
		private byte value;
		
		public byte getValue() {
			return value;
		}
		
	}
	
	private CarrierTypeFormat carrierTypeFormat;
	
	/** The value of the CARRIER_TYPE field gives a unique identification of the
	* alternative carrier (see section 2.5). The value of the CARRIER_TYPE field MUST follow the
	* structure, encoding, and format implied by the value of the CTF field 
	*/
	private Object carrierType;

	/** A sequence of octets that provide additional information about the
	* alternative carrier enquiry. The syntax and semantics of this data are determined by the
	* CARRIER_TYPE field.
	*/
	private Object carrierData;
	
	public HandoverCarrierRecord() {
		super(TYPE);
	}
}
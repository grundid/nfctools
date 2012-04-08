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
 * 
 * The record references are established using the URI-based Payload Identification mechanism described in the NDEF specification [NDEF]. 
 * The URI reference values SHALL be encoded as relative URIs with the virtual base defined as “urn:nfc:handover:”.
 * The message generator is responsible for the uniqueness of the payload identifiers encoded into the ID field of the NDEF record header. 
 * While identifiers can be strings of length up to 255 characters, it is RECOMMENDED that short, possibly single character, strings are used. 
 * However, the generator SHALL NOT use the tilde character (“~”, hexadecimal 7E) at the first string position and a 
 * compliant parser SHALL ignore strings starting with a tilde character.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class AlternativeCarrierRecord extends AbstractWellKnownRecord {

	public static final byte[] TYPE = { 0x61, 0x63 }; // "ac"

	public static enum CarrierPowerState {
		
		Inactive((byte)0x00), Active((byte)0x01), Activating((byte)0x02), Unknown((byte)0x03);
		
		private CarrierPowerState(byte value) {
			this.value = value;
		}
		
		private byte value;
		
		public byte getValue() {
			return value;
		}
		
		public static CarrierPowerState toCarrierPowerState(byte value) {
			
			for(CarrierPowerState state : values()) {
				if(state.value == value) {
					return state;
				}
			}
			
			throw new IllegalArgumentException("Unknown carrier power state " + value);
		}
	}
	
	private CarrierPowerState carrierPowerState;

	private String carrierDataReference;
	
	private List<String> auxiliaryDataReferences;

	public AlternativeCarrierRecord() {
		this(new ArrayList<String>());
	}

	public AlternativeCarrierRecord(List<String> auxiliaryDataReferences) {
		this.auxiliaryDataReferences = auxiliaryDataReferences;
	}

	
	public AlternativeCarrierRecord(CarrierPowerState carrierPowerState, String carrierDataReference) {
		this(carrierPowerState, carrierDataReference, new ArrayList<String>());
	}		
	
	public AlternativeCarrierRecord(CarrierPowerState carrierPowerState, String carrierDataReference, List<String> auxiliaryDataReferences) {
		this(auxiliaryDataReferences);
		
		this.carrierPowerState = carrierPowerState;
		this.carrierDataReference = carrierDataReference;
	}

	public CarrierPowerState getCarrierPowerState() {
		return carrierPowerState;
	}

	public void setCarrierPowerState(CarrierPowerState carrierPowerState) {
		this.carrierPowerState = carrierPowerState;
	}

	public String getCarrierDataReference() {
		return carrierDataReference;
	}

	public void setCarrierDataReference(String carrierDataReference) {
		this.carrierDataReference = carrierDataReference;
	}

	public List<String> getAuxiliaryDataReferences() {
		return auxiliaryDataReferences;
	}

	public void setAuxiliaryDataReferences(List<String> auxiliaryDataReference) {
		this.auxiliaryDataReferences = auxiliaryDataReference;
	}

	public void addAuxiliaryDataReference(String string) {
		this.auxiliaryDataReferences.add(string);
	}
	
	
}
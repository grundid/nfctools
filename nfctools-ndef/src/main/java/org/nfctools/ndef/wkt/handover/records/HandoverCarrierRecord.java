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

import java.util.Arrays;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.ext.UnsupportedExternalTypeRecord;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * The Handover Carrier Record provides a unique identification of an alternative carrier technology in Handover Request
 * messages when no carrier configuration data is to be provided. If the Handover Selector has the same carrier
 * technology available, it would respond with a Carrier Configuration record with payload type equal to the carrier
 * type (that is, the triples {TNF, TYPE_LENGTH, TYPE} and {CTF, CARRIER_TYPE_LENGTH, CARRIER_TYPE} match exactly).
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverCarrierRecord extends WellKnownRecord {

	/** This is a 3-bit field that indicates the structure of the value of the CARRIER_TYPE field. */
	public static enum CarrierTypeFormat {

		WellKnown(NdefConstants.TNF_WELL_KNOWN), Media(NdefConstants.TNF_MIME_MEDIA), AbsoluteURI(
				NdefConstants.TNF_ABSOLUTE_URI), External(NdefConstants.TNF_EXTERNAL_TYPE);

		private CarrierTypeFormat(byte value) {
			this.value = value;
		}

		private byte value;

		public byte getValue() {
			return value;
		}

		public static CarrierTypeFormat toCarrierTypeFormat(byte value) {
			for (CarrierTypeFormat carrierTypeFormat : values()) {
				if (carrierTypeFormat.value == value) {
					return carrierTypeFormat;
				}
			}
			throw new IllegalArgumentException("Unknown carrier type format " + value);
		}
	}

	private CarrierTypeFormat carrierTypeFormat;

	/**
	 * The value of the CARRIER_TYPE field gives a unique identification of the alternative carrier (see section 2.5).
	 * The value of the CARRIER_TYPE field MUST follow the structure, encoding, and format implied by the value of the
	 * CTF field
	 */
	private Object carrierType;

	/**
	 * A sequence of octets that provide additional information about the alternative carrier enquiry. The syntax and
	 * semantics of this data are determined by the CARRIER_TYPE field.
	 */
	private byte[] carrierData;

	public HandoverCarrierRecord(CarrierTypeFormat carrierTypeFormat, UnsupportedExternalTypeRecord carrierType, byte[] carrierData) {
		this.carrierTypeFormat = carrierTypeFormat;
		this.carrierType = carrierType;
		this.carrierData = carrierData;
	}

	public HandoverCarrierRecord(CarrierTypeFormat carrierTypeFormat, WellKnownRecord carrierType, byte[] carrierData) {
		this.carrierTypeFormat = carrierTypeFormat;
		this.carrierType = carrierType;
		this.carrierData = carrierData;
	}

	public HandoverCarrierRecord() {
	}

	public HandoverCarrierRecord(CarrierTypeFormat carrierTypeFormat, String carrierType, byte[] carrierData) {
		this.carrierTypeFormat = carrierTypeFormat;
		this.carrierType = carrierType;
		this.carrierData = carrierData;
	}

	public CarrierTypeFormat getCarrierTypeFormat() {
		return carrierTypeFormat;
	}

	public void setCarrierTypeFormat(CarrierTypeFormat carrierTypeFormat) {
		this.carrierTypeFormat = carrierTypeFormat;
	}

	public Object getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(Object carrierType) {
		this.carrierType = carrierType;
	}

	public byte[] getCarrierData() {
		return carrierData;
	}

	public void setCarrierData(byte[] carrierData) {
		this.carrierData = carrierData;
	}

	public boolean hasCarrierData() {
		return carrierData != null;
	}

	public int getCarrierDataSize() {
		return carrierData.length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(carrierData);
		result = prime * result + ((carrierType == null) ? 0 : carrierType.hashCode());
		result = prime * result + ((carrierTypeFormat == null) ? 0 : carrierTypeFormat.hashCode());
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
		HandoverCarrierRecord other = (HandoverCarrierRecord)obj;
		if (!Arrays.equals(carrierData, other.carrierData))
			return false;
		if (carrierType == null) {
			if (other.carrierType != null)
				return false;
		}
		else if (!carrierType.equals(other.carrierType))
			return false;
		if (carrierTypeFormat != other.carrierTypeFormat)
			return false;
		return true;
	}

	public boolean hasCarrierTypeFormat() {
		return carrierTypeFormat != null;
	}

	public boolean hasCarrierType() {
		return carrierType != null;
	}

}
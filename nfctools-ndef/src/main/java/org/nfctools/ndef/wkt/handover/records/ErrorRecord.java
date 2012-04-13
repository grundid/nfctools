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

import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * The Error Record is used in the Handover Select Record to indicate that the Handover Selector failed to successfully
 * process the most recently received Handover Request Message. It SHALL NOT be used elsewhere.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class ErrorRecord extends WellKnownRecord {

	/**
	 * An 8-bit field that indicates the specific type of error that caused the Handover Selector to return the Error
	 * Record
	 */

	public static enum ErrorReason {
		/**
		 * The Handover Request Message could not be processed due to temporary memory constraints. Resending the
		 * unmodified Handover Request Message might be successful after a time interval of at least the number of
		 * milliseconds expressed in the error data field.
		 */

		TemporaryMemoryConstraints((byte)0x01),
		/**
		 * The Handover Request Message could not be processed due to permanent memory constraints. Resending the
		 * unmodified Handover Request Message will always yield the same error condition.
		 */
		PermanenteMemoryConstraints((byte)0x02),
		/**
		 * The Handover Request Message could not be processed due to carrier-specific constraints. Resending the
		 * Handover Request Message might not be successful until after a time interval of at least the number of
		 * milliseconds expressed in the error data field.
		 */
		CarrierSpecificConstraints((byte)0x03);

		private ErrorReason(byte value) {
			this.value = value;
		}

		private byte value;

		public byte getValue() {
			return value;
		}

		public static ErrorReason toErrorReason(byte errorReason) {
			if (errorReason == TemporaryMemoryConstraints.value) {
				return TemporaryMemoryConstraints;
			}
			else if (errorReason == PermanenteMemoryConstraints.value) {
				return PermanenteMemoryConstraints;
			}
			else if (errorReason == CarrierSpecificConstraints.value) {
				return CarrierSpecificConstraints;
			}
			throw new IllegalArgumentException("Unexpected error reason code " + errorReason);
		}
	}

	private ErrorReason errorReason;

	/**
	 * A sequence of octets providing additional information about the conditions that caused the handover selector to
	 * enter erroneous state. The syntax and semantics of this data are determined by the ERROR_REASON field and are
	 * specified in Table 4. The number of octets encoded in the ERROR_DATA field SHALL be determined by the number of
	 * octets in the payload of the Error Record minus 1.
	 */

	private Number errorData;

	public ErrorRecord() {
	}

	public ErrorRecord(ErrorReason errorReason, Number errorData) {
		this.errorReason = errorReason;
		this.errorData = errorData;
	}

	public ErrorReason getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(ErrorReason errorReason) {
		this.errorReason = errorReason;
	}

	public Number getErrorData() {
		return errorData;
	}

	public void setErrorData(Number errorData) {
		this.errorData = errorData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((errorData == null) ? 0 : errorData.hashCode());
		result = prime * result + ((errorReason == null) ? 0 : errorReason.hashCode());
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
		ErrorRecord other = (ErrorRecord)obj;
		if (errorData == null) {
			if (other.errorData != null)
				return false;
		}
		else if (!errorData.equals(other.errorData))
			return false;
		if (errorReason != other.errorReason)
			return false;
		return true;
	}

	public boolean hasErrorReason() {
		return errorReason != null;
	}

	public boolean hasErrorData() {
		return errorData != null;
	}

}

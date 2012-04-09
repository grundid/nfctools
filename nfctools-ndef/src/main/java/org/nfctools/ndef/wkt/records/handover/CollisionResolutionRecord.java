package org.nfctools.ndef.wkt.records.handover;

import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * The Collision Resolution Record is used in the Handover Request Record to transmit the random number 
 * required to resolve a collision of handover request messages. It SHALL NOT be used elsewhere.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 *
 */

public class CollisionResolutionRecord extends WellKnownRecord {

	public static final byte[] TYPE = {0x63, 0x72 }; // "cr"

	/** This 16-bit field contains an integer number that SHALL be randomly generated before sending a Handover Request Message */
	private short randomNumber;

	public CollisionResolutionRecord() {
	}

	public CollisionResolutionRecord(short randomNumber) {
		this.randomNumber = randomNumber;
	}	
	
	public short getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(short randomNumber) {
		this.randomNumber = randomNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + randomNumber;
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
		CollisionResolutionRecord other = (CollisionResolutionRecord) obj;
		if (randomNumber != other.randomNumber)
			return false;
		return true;
	}
	
	
}

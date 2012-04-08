package org.nfctools.ndef.wkt.records.handover;

import org.nfctools.ndef.wkt.records.AbstractWellKnownRecord;

/**
 * 
 * The Collision Resolution Record is used in the Handover Request Record to transmit the random number 
 * required to resolve a collision of handover request messages. It SHALL NOT be used elsewhere.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 *
 */

public class CollisionResolutionRecord extends AbstractWellKnownRecord {

	public static final byte[] TYPE = {0x63, 0x72 }; // "cr"

	/** This 16-bit field contains an integer number that SHALL be randomly generated before sending a Handover Request Message */
	private short randomNumber;

	public short getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(short randomNumber) {
		this.randomNumber = randomNumber;
	}
	
	
}

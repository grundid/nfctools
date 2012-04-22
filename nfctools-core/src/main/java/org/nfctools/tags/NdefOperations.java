package org.nfctools.tags;

import org.nfctools.ndef.NdefMessage;

/**
 * Some methods are inspired by the Ndef/NdefFormatable classes from the Android Project.
 * 
 * TODO move it to the API package
 * 
 * @author adrian
 * 
 */
public interface NdefOperations {

	/**
	 * Get the maximum NDEF message size in bytes.
	 */
	int getMaxSize();

	boolean hasNdefMessage();

	boolean isFormated();

	/**
	 * Read the current NdefMessage on this tag.
	 */
	NdefMessage getNdefMessage();

	/**
	 * Overwrite the NdefMessage on this tag.
	 */
	void writeNdefMessage(NdefMessage ndefMessage);

	/**
	 * Determine if the tag is writable.
	 */
	boolean isWritable();

	/**
	 * Make a tag read-only.
	 */
	boolean makeReadOnly();

	/**
	 * Format a tag as NDEF, and write a NdefMessage.
	 */
	void format(NdefMessage ndefMessage);

	/**
	 * Formats a tag as NDEF, write a NdefMessage, and make read-only.
	 */
	void formatReadOnly(NdefMessage ndefMessage);
}

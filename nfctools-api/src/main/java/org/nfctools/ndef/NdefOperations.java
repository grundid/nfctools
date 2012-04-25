package org.nfctools.ndef;

import java.util.List;


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

	boolean isFormatted();

	/**
	 * Determine if the tag is writable.
	 */
	boolean isWritable();

	/**
	 * Read the current NdefMessage on this tag.
	 */
	List<Record> readNdefMessage();

	/**
	 * Overwrite the NdefMessage on this tag.
	 */
	void writeNdefMessage(Record... records);

	/**
	 * Make a tag read-only.
	 */
	void makeReadOnly();

	/**
	 * Format a tag as NDEF.
	 */
	void format();

	/**
	 * Format a tag as NDEF, and write a NdefMessage.
	 */
	void format(Record... records);

	/**
	 * Formats a tag as NDEF, write a NdefMessage, and make read-only.
	 */
	void formatReadOnly(Record... records);
}

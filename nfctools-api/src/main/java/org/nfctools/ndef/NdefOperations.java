/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
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
package org.nfctools.ndef;

import java.util.List;

import org.nfctools.api.TagInfo;

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

	/**
	 * 
	 * @return additional information about the tag like the ID
	 */
	TagInfo getTagInfo();
}

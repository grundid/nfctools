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
package org.nfctools.test;

import java.io.IOException;

import org.nfctools.mf.ul.MemoryLayout;
import org.nfctools.mf.ul.Type2NdefOperations;
import org.nfctools.spi.acs.AcrMfUlReaderWriter;

public abstract class TestConfigs {

	public static final String[] TYPE2_BLANK_TAGS = { "mfulc_blank.txt", "mful_blank.txt" };

	public static Type2NdefOperations getType2BlankTag(String fileName) {
		return getType2BlankTag(fileName, false, true);
	}

	public static Type2NdefOperations getType2BlankTag(String fileName, boolean formatted, boolean writable) {
		try {
			InMemoryTag tag = new InMemoryTag(FileMfUlReader.loadCardFromFile(fileName));
			AcrMfUlReaderWriter readerWriter = new AcrMfUlReaderWriter(tag);
			return new Type2NdefOperations(fileName.startsWith("mful_") ? MemoryLayout.ULTRALIGHT
					: MemoryLayout.ULTRALIGHT_C, readerWriter, readerWriter.getTagInfo(), formatted, writable);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

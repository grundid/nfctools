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
package org.nfctools.mf.classic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.nfctools.mf.ul.MemoryMap;
import org.nfctools.ndef.Record;
import org.nfctools.spi.acs.AcrMfClassicReaderWriter;
import org.nfctools.test.FileMfClassicReader;
import org.nfctools.test.InMemoryTag;

public class MfClassicNdefOperationsTest {

	private MfClassicNdefOperations ndefOperations;
	private AcrMfClassicReaderWriter readerWriter;
	private InMemoryTag tag;
	private InMemoryTag expectedTag;

	private static class Config {

		public Config(String fileName, MemoryLayout memoryLayout, boolean formatted, boolean writeable, String expected) {
			this.fileName = fileName;
			this.memoryLayout = memoryLayout;
			this.formatted = formatted;
			this.writeable = writeable;
			this.expected = expected;
		}

		public Config(String fileName, MemoryLayout memoryLayout, boolean formatted, boolean writeable,
				String expected, int expectedSize) {
			this.fileName = fileName;
			this.memoryLayout = memoryLayout;
			this.formatted = formatted;
			this.writeable = writeable;
			this.expected = expected;
			this.expectedSize = expectedSize;
		}

		String fileName;
		MemoryLayout memoryLayout;
		boolean formatted;
		boolean writeable;
		String expected;
		int expectedSize;

		@Override
		public String toString() {
			return "Config: " + fileName + " expected: " + expected;
		}
	}

	private static final Config[] READ_TEST = {
			new Config("mfstd1k_ndef.txt", MemoryLayout.CLASSIC_1K, true, true, null),
			new Config("mfstd4k_ndef.txt", MemoryLayout.CLASSIC_4K, true, true, null) };
	private static final Config[] FORMAT_TEST = {
			new Config("mfstd1k_blank.txt", MemoryLayout.CLASSIC_1K, false, true, "mfstd1k_formatted.txt", 720),
			new Config("mfstd4k_blank.txt", MemoryLayout.CLASSIC_4K, false, true, "mfstd4k_formatted.txt", 3360) };
	private static final Config[] MAKE_READ_ONLY_TEST = {
			new Config("mfstd1k_formatted.txt", MemoryLayout.CLASSIC_1K, true, true, "mfstd1k_formatted_readonly.txt"),
			new Config("mfstd4k_formatted.txt", MemoryLayout.CLASSIC_4K, true, true, "mfstd4k_formatted_readonly.txt") };

	private void init(Config config) throws IOException {
		MemoryMap memoryMap = FileMfClassicReader.loadCardFromFile(config.fileName);
		tag = new InMemoryTag(memoryMap);
		readerWriter = new AcrMfClassicReaderWriter(tag, config.memoryLayout);
		ndefOperations = new MfClassicNdefOperations(readerWriter, readerWriter.getTagInfo(), config.formatted,
				config.writeable);
		if (config.expected != null && config.expected.startsWith("mfstd")) {
			expectedTag = new InMemoryTag(FileMfClassicReader.loadCardFromFile(config.expected));
		}
	}

	@Test
	public void testReadNewMessages() throws Exception {
		for (Config config : READ_TEST) {
			init(config);
			List<Record> ndefMessage = ndefOperations.readNdefMessage();
			assertEquals(1, ndefMessage.size());
			//			System.out.println(ndefMessage.get(0));
			//			SmartPosterRecord record = (SmartPosterRecord)ndefMessage.get(0);
			//			assertEquals("Hallo, this is a SmartPosterTag for NFC Tools", record.getTitle().getText());
			//			assertEquals("http://www.nfctools.org", record.getUri().getUri());
		}
	}

	@Test
	public void testFormat() throws Exception {
		for (Config config : FORMAT_TEST) {
			init(config);
			assertFalse(ndefOperations.isFormatted());
			ndefOperations.format();
			assertTrue(ndefOperations.isFormatted());
			assertEquals(config.expectedSize, ndefOperations.getMaxSize());
			assertArrayEquals(expectedTag.getMemoryMap().getMemory(), tag.getMemoryMap().getMemory());
		}
	}

	@Test
	public void testMakeReadOnly() throws Exception {
		for (Config config : MAKE_READ_ONLY_TEST) {
			init(config);
			assertTrue(ndefOperations.isWritable());
			ndefOperations.makeReadOnly();
			assertFalse(ndefOperations.isWritable());
			assertArrayEquals(expectedTag.getMemoryMap().getMemory(), tag.getMemoryMap().getMemory());
		}
	}
}

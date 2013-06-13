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
package org.nfctools.mf.ul;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.nfctools.ndef.NotEnoughMemoryException;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.spi.acs.AcrMfUlReaderWriter;
import org.nfctools.test.FileMfUlReader;
import org.nfctools.test.InMemoryTag;

public class Type2NdefOperationsTest {

	private InMemoryTag tag;
	private AcrMfUlReaderWriter readerWriter;
	private Type2NdefOperations ndefOperations;
	private InMemoryTag expectedTag;

	private static class Config {

		public Config(String fileName, MemoryLayout memoryLayout, boolean formatted, boolean writeable,
				String expectedFileName) {
			this.fileName = fileName;
			this.memoryLayout = memoryLayout;
			this.formatted = formatted;
			this.writeable = writeable;
			this.expectedFileName = expectedFileName;
		}

		String fileName;
		MemoryLayout memoryLayout;
		boolean formatted;
		boolean writeable;
		String expectedFileName;

		@Override
		public String toString() {
			return "Config: " + fileName + " expected: " + expectedFileName;
		}
	}

	private static final Config[] FORMAT_TEST = {
			new Config("mful_blank.txt", MemoryLayout.ULTRALIGHT, false, true, "mful_formatted.txt"),
			new Config("mfulc_blank.txt", MemoryLayout.ULTRALIGHT_C, false, true, "mfulc_formatted.txt") };
	private static final Config[] FORMAT_READ_ONLY_TEST = {
			new Config("mful_blank.txt", MemoryLayout.ULTRALIGHT, false, true, "mful_formatted_readonly.txt"),
			new Config("mfulc_blank.txt", MemoryLayout.ULTRALIGHT_C, false, true, "mfulc_formatted_readonly.txt") };
	private static final Config[] MAKE_READ_ONLY_TEST = {
			new Config("mful_formatted.txt", MemoryLayout.ULTRALIGHT, true, true, "mful_formatted_readonly.txt"),
			new Config("mfulc_formatted.txt", MemoryLayout.ULTRALIGHT_C, true, true, "mfulc_formatted_readonly.txt") };
	private static final Config[] WRITE_TEST = {
			new Config("mful_formatted.txt", MemoryLayout.ULTRALIGHT, true, true, "mful_ndef.txt"),
			new Config("mfulc_formatted.txt", MemoryLayout.ULTRALIGHT_C, true, true, "mfulc_ndef.txt") };
	private static final Config[] READ_TEST = { new Config("mful_ndef.txt", MemoryLayout.ULTRALIGHT, true, true, null),
			new Config("mfulc_ndef.txt", MemoryLayout.ULTRALIGHT_C, true, true, null) };
	private static final Config[] READ_EMPTY_TEST = {
			new Config("mful_formatted.txt", MemoryLayout.ULTRALIGHT, true, true, null),
			new Config("mfulc_formatted.txt", MemoryLayout.ULTRALIGHT_C, true, true, null) };

	private void init(Config config) {
		try {
			tag = new InMemoryTag(FileMfUlReader.loadCardFromFile(config.fileName));
			readerWriter = new AcrMfUlReaderWriter(tag);
			ndefOperations = new Type2NdefOperations(config.memoryLayout, readerWriter, readerWriter.getTagInfo(),
					config.formatted, config.writeable);
			if (config.expectedFileName != null)
				expectedTag = new InMemoryTag(FileMfUlReader.loadCardFromFile(config.expectedFileName));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testFormat() throws Exception {
		for (Config config : FORMAT_TEST) {
			init(config);
			ndefOperations.format();
			assertArrayEquals(config.toString(), expectedTag.getMemoryMap().getMemory(), tag.getMemoryMap().getMemory());
		}
	}

	@Test
	public void testFormatReadOnly() throws Exception {
		for (Config config : FORMAT_READ_ONLY_TEST) {
			init(config);
			ndefOperations.formatReadOnly(new Record[0]);
			assertArrayEquals(config.toString(), expectedTag.getMemoryMap().getMemory(), tag.getMemoryMap().getMemory());
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

	@Test
	public void testWriteNdefMessages() throws Exception {
		for (Config config : WRITE_TEST) {
			init(config);
			ndefOperations.writeNdefMessage(new UriRecord("http://example.com/some/more/more/1234/data.html"));
			assertArrayEquals(expectedTag.getMemoryMap().getMemory(), tag.getMemoryMap().getMemory());
		}
	}

	@Test
	public void testWriteNdefMessagesTooLong() throws Exception {
		for (Config config : WRITE_TEST) {
			try {
				init(config);
				ndefOperations.writeNdefMessage(new UriRecord("http://example.com/some/more/data.html?"
						+ "someparameter=test&someparameter=test&someparameter=test&someparameter=test&"
						+ "someparameter=test&someparameter=test&someparameter=test&someparameter=test&"
						+ "someparameter=test&someparameter=test&someparameter=test&someparameter=test&"
						+ "someparameter=test&someparameter=test&someparameter=test&someparameter=test&"
						+ "someparameter=test&someparameter=test&someparameter=test&someparameter=test"));
				fail("Exception expected with " + config);
			}
			catch (NotEnoughMemoryException e) {
			}
		}
	}

	@Test
	public void testReadNdefMessages() throws Exception {
		for (Config config : READ_TEST) {
			init(config);
			List<Record> ndefMessage = ndefOperations.readNdefMessage();
			assertEquals(1, ndefMessage.size());
			UriRecord record = (UriRecord)ndefMessage.get(0);
			assertEquals("http://example.com/some/more/more/1234/data.html", record.getUri());
		}
	}

	@Test
	public void testReadNdefMessagesEmpty() throws Exception {
		for (Config config : READ_EMPTY_TEST) {
			init(config);
			List<Record> ndefMessage = ndefOperations.readNdefMessage();
			assertEquals(0, ndefMessage.size());
		}
	}
}

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
package org.nfctools.tags;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.mf.ul.MemoryLayout;
import org.nfctools.mf.ul.MfUlReaderWriter;
import org.nfctools.spi.acs.AcrMfUlReaderWriter;
import org.nfctools.test.FileMfUlReader;
import org.nfctools.test.InMemoryTag;

public class TagInputStreamTest {

	@Test
	public void testAvailable() throws Exception {
		InMemoryTag tag = new InMemoryTag(FileMfUlReader.loadCardFromFile("mful_formatted.txt"));
		MfUlReaderWriter readerWriter = new AcrMfUlReaderWriter(tag);

		TagInputStream in = new TagInputStream(MemoryLayout.ULTRALIGHT, readerWriter);

		for (int x = 0; x < 48; x++) {
			assertEquals(48 - x, in.available());
			in.read();
		}
		assertEquals(0, in.available());
	}
}

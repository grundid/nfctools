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

import org.junit.Test;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.test.FileMfUlReader;
import org.nfctools.test.MemoryMapUlReaderWriter;

public class UltralightTest {

	@Test
	public void testIsBlank() throws Exception {
		MemoryMap memoryMap = FileMfUlReader.loadCardFromFile("mful_blank.txt");
		MfUlReaderWriter readerWriter = new MemoryMapUlReaderWriter(memoryMap);
		MfBlock[] blocks = readerWriter.readBlock(0, 5);
		assertTrue(UltralightHandler.isBlank(blocks));
	}

	@Test
	public void testIsFormatted() throws Exception {
		MemoryMap memoryMap = FileMfUlReader.loadCardFromFile("mfulc_formatted.txt");
		MfUlReaderWriter readerWriter = new MemoryMapUlReaderWriter(memoryMap);
		MfBlock[] blocks = readerWriter.readBlock(0, 5);
		assertTrue(UltralightHandler.isFormatted(blocks));
	}

}

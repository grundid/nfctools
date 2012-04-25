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
import org.nfctools.test.FileMfUlReader;

public class MemoryMapTest {

	@Test
	public void testLoadFile() throws Exception {
		MemoryMap memoryMap = FileMfUlReader.loadCardFromFile("mfulc_formatted.txt");
		assertArrayEquals(new byte[] { 0x04, (byte)0xCE, (byte)0x8F, (byte)0xCD }, memoryMap.getPage(0));
		assertArrayEquals(new byte[] { (byte)0xE1, 0x10, 0x12, 0x00 }, memoryMap.getPage(3));
	}
}

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

import org.junit.Test;
import org.nfctools.mf.ul.MemoryMap;
import org.nfctools.test.FileMfClassicReader;
import org.nfctools.test.InMemoryTag;

public class MfClassicNfcTagListenerTest {

	@Test
	public void testCreateNdefOperations() throws Exception {
		MemoryMap memoryMap = FileMfClassicReader.loadCardFromFile("mfstd1k_blank.txt");
		InMemoryTag tag = new InMemoryTag(memoryMap);

		MfClassicNfcTagListener nfcTagListener = new MfClassicNfcTagListener();

		MfClassicNdefOperations ndefOperations = nfcTagListener.createNdefOperations(tag, MemoryLayout.CLASSIC_1K);
		assertNotNull(ndefOperations);

		assertFalse(ndefOperations.isFormatted());
		assertTrue(ndefOperations.isWritable());

	}
}

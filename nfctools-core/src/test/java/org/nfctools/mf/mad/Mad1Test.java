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
package org.nfctools.mf.mad;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.classic.MfClassicReaderWriter;

public class Mad1Test extends AbstractMadTests {

	public Mad1Test() {
		super("mfstd1k_blank.txt", "mfstd1k_ndef.txt", 720, 96, 96, MemoryLayout.CLASSIC_1K);
	}

	@Test
	public void testMadSpec() throws Exception {
		Mad1 mad = new Mad1(loadData(blankCard), MAD_KEY_CONFIG);
		assertEquals(15, mad.getNumberOfSlots());
		assertEquals(1, mad.getSectorIdForSlot(0));
		assertEquals(15, mad.getSectorIdForSlot(14));

		assertEquals(3 * 16, mad.getSlotSize(0));
		assertEquals(3 * 16, mad.getSlotSize(14));
	}

	@Test
	public void testMadAidSize() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(blankCard);

		ApplicationDirectory applicationDirectory = readerWriter.createApplicationDirectory(MAD_KEY_CONFIG);

		assertEquals(1, applicationDirectory.getVersion());

		assertTrue(applicationDirectory.isFree(0));
		assertTrue(applicationDirectory.isFree(applicationDirectory.getNumberOfSlots() - 1));

		assertEquals(maxFreeSpace, applicationDirectory.getMaxContinousSize());
	}

	@Test
	public void testMadCrc() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(cardWithMad);
		Mad1 applicationDirectory = (Mad1)readerWriter.getApplicationDirectory(MAD_KEY_CONFIG);
		applicationDirectory.updateCrc();
		assertEquals((byte)0xf3, applicationDirectory.madData[0]);

	}
}

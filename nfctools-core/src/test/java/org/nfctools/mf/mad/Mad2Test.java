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
import org.nfctools.mf.Key;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.card.MfCard;
import org.nfctools.spi.file.FileMfReaderWriter;

public class Mad2Test extends AbstractMadTests {

	public Mad2Test() {
		super("mfstd4k_00.txt", "mfstd4k_01.txt", 3360, 2160, 3360);
	}

	@Test
	public void testMadSpec() throws Exception {
		Mad2 mad2 = new Mad2(null, null, null);
		assertEquals(38, mad2.getNumberOfSlots());
		assertEquals(1, mad2.getSectorIdForSlot(0));
		assertEquals(15, mad2.getSectorIdForSlot(14));
		assertEquals(17, mad2.getSectorIdForSlot(15));
		assertEquals(39, mad2.getSectorIdForSlot(37));

		assertEquals(3 * 16, mad2.getSlotSize(0));
		assertEquals(3 * 16, mad2.getSlotSize(14));
		assertEquals(3 * 16, mad2.getSlotSize(15));
		assertEquals(3 * 16, mad2.getSlotSize(29));
		assertEquals(15 * 16, mad2.getSlotSize(30));
		assertEquals(15 * 16, mad2.getSlotSize(37));
	}

	@Test
	public void testMadAidSize() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(emptyCard);
		ApplicationDirectory applicationDirectory = MadUtils.createApplicationDirectory(mfCard, mfReaderWriter, Key.A,
				MfConstants.TRANSPORT_KEY, dummyKey);

		assertEquals(2, applicationDirectory.getVersion());

		assertTrue(applicationDirectory.isFree(0));
		assertTrue(applicationDirectory.isFree(applicationDirectory.getNumberOfSlots() - 1));

		assertEquals(maxFreeSpace, applicationDirectory.getMaxContinousSize());
	}

}

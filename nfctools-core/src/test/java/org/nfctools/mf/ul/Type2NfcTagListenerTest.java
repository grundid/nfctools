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
import org.nfctools.test.TestConfigs;

public class Type2NfcTagListenerTest {

	@Test
	public void testCreateNdefOperationsBlank() throws Exception {
		for (String tagFileName : TestConfigs.TYPE2_BLANK_TAGS) {
			Type2NdefOperations ndefOperations = TestConfigs.getType2BlankTag(tagFileName);
			assertTrue(ndefOperations.isWritable());
			assertFalse(ndefOperations.isFormatted());
		}
	}

	@Test
	public void testCreateNdefOperationsFormatted() throws Exception {
		Type2NdefOperations ndefOperations = TestConfigs.getType2BlankTag("mful_formatted.txt", true, true);
		assertTrue(ndefOperations.isWritable());
		assertTrue(ndefOperations.isFormatted());
	}

	@Test
	public void testCreateNdefOperationsFormattedReadOnly() throws Exception {
		Type2NdefOperations ndefOperations = TestConfigs.getType2BlankTag("mful_formatted_readonly.txt", true, false);
		assertFalse(ndefOperations.isWritable());
		assertTrue(ndefOperations.isFormatted());
	}
}

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
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.mad.ApplicationDirectory;
import org.nfctools.test.ReaderWriterCreator;

public class ClassicHandlerTest {

	@Test
	public void testIsBlank() throws Exception {
		assertTrue(ClassicHandler.isBlank(ReaderWriterCreator.createReadWriter("mfstd1k_blank.txt",
				MemoryLayout.CLASSIC_1K)));
		assertTrue(ClassicHandler.isBlank(ReaderWriterCreator.createReadWriter("mfstd4k_blank.txt",
				MemoryLayout.CLASSIC_4K)));
		assertFalse(ClassicHandler.isBlank(ReaderWriterCreator.createReadWriter("mfstd1k_ndef.txt",
				MemoryLayout.CLASSIC_1K)));
		assertFalse(ClassicHandler.isBlank(ReaderWriterCreator.createReadWriter("mfstd4k_ndef.txt",
				MemoryLayout.CLASSIC_4K)));
	}

	@Test
	public void testIsFormattedWritable() throws Exception {
		ApplicationDirectory applicationDirectory = ReaderWriterCreator.createReadWriter("mfstd1k_ndef.txt",
				MemoryLayout.CLASSIC_1K).getApplicationDirectory();
		assertTrue(ClassicHandler.isFormattedWritable(applicationDirectory.openApplication(MfConstants.NDEF_APP_ID),
				MfClassicConstants.TRANSPORT_KEY));
		ApplicationDirectory applicationDirectory2 = ReaderWriterCreator.createReadWriter("mfstd4k_ndef.txt",
				MemoryLayout.CLASSIC_4K).getApplicationDirectory();
		assertTrue(ClassicHandler.isFormattedWritable(applicationDirectory2.openApplication(MfConstants.NDEF_APP_ID),
				MfClassicConstants.NDEF_KEY));
	}
}

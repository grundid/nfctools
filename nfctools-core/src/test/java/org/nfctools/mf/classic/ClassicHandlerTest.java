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
		assertTrue(ClassicHandler.isFormattedWritable(applicationDirectory.openApplication(MfConstants.NDEF_APP_ID)));

		ApplicationDirectory applicationDirectory2 = ReaderWriterCreator.createReadWriter("mfstd4k_ndef.txt",
				MemoryLayout.CLASSIC_4K).getApplicationDirectory();

		assertTrue(ClassicHandler.isFormattedWritable(applicationDirectory2.openApplication(MfConstants.NDEF_APP_ID)));
	}
}

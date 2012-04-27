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

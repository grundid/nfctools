package org.nfctools.mf.ul;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.test.InMemoryUltralightTag;
import org.nfctools.test.TestConfigs;

public class Type2NfcTagListenerTest {

	private Type2NfcTagListener nfcTagListener = new Type2NfcTagListener();

	@Test
	public void testCanHandle() throws Exception {
		for (String tagFileName : TestConfigs.TYPE2_BLANK_TAGS) {
			assertTrue(nfcTagListener.canHandle(new InMemoryUltralightTag(tagFileName)));
		}
	}

	@Test
	public void testCreateNdefOperationsBlank() throws Exception {
		for (String tagFileName : TestConfigs.TYPE2_BLANK_TAGS) {
			InMemoryUltralightTag tag = new InMemoryUltralightTag(tagFileName);
			Type2NdefOperations ndefOperations = nfcTagListener.createNdefOperations(tag);
			assertTrue(ndefOperations.isWritable());
			assertFalse(ndefOperations.isFormatted());
		}
	}

	@Test
	public void testCreateNdefOperationsFormatted() throws Exception {
		InMemoryUltralightTag tag = new InMemoryUltralightTag("mful_formatted.txt");
		Type2NdefOperations ndefOperations = nfcTagListener.createNdefOperations(tag);
		assertTrue(ndefOperations.isWritable());
		assertTrue(ndefOperations.isFormatted());
	}

	@Test
	public void testCreateNdefOperationsFormattedReadOnly() throws Exception {
		InMemoryUltralightTag tag = new InMemoryUltralightTag("mful_formatted_readonly.txt");
		Type2NdefOperations ndefOperations = nfcTagListener.createNdefOperations(tag);
		assertFalse(ndefOperations.isWritable());
		assertTrue(ndefOperations.isFormatted());
	}
}

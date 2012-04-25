package org.nfctools.tags;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.mf.ul.MemoryLayout;
import org.nfctools.mf.ul.MfUlReaderWriter;
import org.nfctools.spi.acs.AcrMfUlReaderWriter;
import org.nfctools.test.InMemoryUltralightTag;

public class TagInputStreamTest {

	@Test
	public void testAvailable() throws Exception {
		InMemoryUltralightTag tag = new InMemoryUltralightTag("mful_formatted.txt");
		MfUlReaderWriter readerWriter = new AcrMfUlReaderWriter(tag);

		TagInputStream in = new TagInputStream(MemoryLayout.ULTRALIGHT, readerWriter);

		for (int x = 0; x < 48; x++) {
			assertEquals(48 - x, in.available());
			in.read();
		}
		assertEquals(0, in.available());
	}
}

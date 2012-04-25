package org.nfctools.mf.ul;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.test.FileMfUlReader;
import org.nfctools.test.MemoryMapUlReaderWriter;

public class UltralightTest {

	@Test
	public void testIsBlank() throws Exception {
		MemoryMap memoryMap = FileMfUlReader.loadCardFromFile("mful_blank.txt");
		MfUlReaderWriter readerWriter = new MemoryMapUlReaderWriter(memoryMap);
		MfBlock[] blocks = readerWriter.readBlock(0, 5);
		assertTrue(UltralightHandler.isBlank(blocks));
	}

	@Test
	public void testIsFormatted() throws Exception {
		MemoryMap memoryMap = FileMfUlReader.loadCardFromFile("mfulc_formatted.txt");
		MfUlReaderWriter readerWriter = new MemoryMapUlReaderWriter(memoryMap);
		MfBlock[] blocks = readerWriter.readBlock(0, 5);
		assertTrue(UltralightHandler.isFormatted(blocks));
	}

}

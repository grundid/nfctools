package org.nfctools.mf.ul;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.test.FileMfUlReader;

public class MemoryMapTest {

	@Test
	public void testLoadFile() throws Exception {
		MemoryMap memoryMap = FileMfUlReader.loadCardFromFile("mfulc_formatted.txt");
		assertArrayEquals(new byte[] { 0x04, (byte)0xCE, (byte)0x8F, (byte)0xCD }, memoryMap.getPage(0));
		assertArrayEquals(new byte[] { (byte)0xE1, 0x10, 0x12, 0x00 }, memoryMap.getPage(3));
	}
}

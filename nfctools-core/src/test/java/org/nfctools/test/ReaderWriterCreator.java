package org.nfctools.test;

import java.io.IOException;

import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.ul.MemoryMap;
import org.nfctools.spi.acs.AcrMfClassicReaderWriter;

public class ReaderWriterCreator {

	public static AcrMfClassicReaderWriter createReadWriter(String fileName, MemoryLayout memoryLayout)
			throws IOException {
		MemoryMap memoryMap = FileMfClassicReader.loadCardFromFile(fileName);
		InMemoryTag tag = new InMemoryTag(memoryMap);
		AcrMfClassicReaderWriter readerWriter = new AcrMfClassicReaderWriter(tag, memoryLayout);
		return readerWriter;
	}

}

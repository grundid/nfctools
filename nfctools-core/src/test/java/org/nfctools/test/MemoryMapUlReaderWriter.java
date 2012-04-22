package org.nfctools.test;

import java.io.IOException;

import javax.smartcardio.Card;

import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.ul.DataBlock;
import org.nfctools.mf.ul.MemoryMap;
import org.nfctools.mf.ul.MfUlReaderWriter;

public class MemoryMapUlReaderWriter implements MfUlReaderWriter {

	private MemoryMap memoryMap;

	public MemoryMapUlReaderWriter(MemoryMap memoryMap) {
		this.memoryMap = memoryMap;
	}

	public MemoryMap getMemoryMap() {
		return memoryMap;
	}

	@Override
	public MfBlock[] readBlock(Card card, int startPage, int pagesToRead) throws IOException {
		MfBlock[] blocks = new MfBlock[pagesToRead];

		for (int x = 0; x < pagesToRead; x++) {
			blocks[x] = new DataBlock(memoryMap.getPage(startPage + x));
		}

		return blocks;
	}

	@Override
	public void writeBlock(Card card, int startPage, MfBlock... mfBlock) throws IOException {
		for (int x = 0; x < mfBlock.length; x++) {
			memoryMap.setPage(startPage + x, mfBlock[x].getData());
		}
	}
}

package org.nfctools.tags;

import java.io.IOException;
import java.io.InputStream;

import org.nfctools.NfcException;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.ul.MemoryLayout;
import org.nfctools.mf.ul.MfUlReaderWriter;

public class TagInputStream extends InputStream {

	private MemoryLayout memoryLayout;
	private MfUlReaderWriter readerWriter;

	private byte[] currentBlock;
	private int currentPage;
	private int currentByte;

	public TagInputStream(MemoryLayout memoryLayout, MfUlReaderWriter readerWriter) {
		this.memoryLayout = memoryLayout;
		this.readerWriter = readerWriter;
		currentPage = memoryLayout.getFirstDataPage();
	}

	private void readNextBlock() {
		try {
			MfBlock[] blocks = readerWriter.readBlock(currentPage, 1);
			currentBlock = blocks[0].getData();
			currentPage++;
			currentByte = 0;
		}
		catch (IOException e) {
			new NfcException(e);
		}
	}

	@Override
	public int available() throws IOException {
		if (currentBlock == null) {
			return memoryLayout.getMaxSize();
		}
		else {
			return (memoryLayout.getLastDataPage() - currentPage + 1) * memoryLayout.getBytesPerPage()
					+ (memoryLayout.getBytesPerPage() - currentByte);
		}
	}

	@Override
	public int read() throws IOException {
		if (currentBlock == null || currentByte >= currentBlock.length) {
			if (available() > 0)
				readNextBlock();
			else
				return -1;
		}

		byte returnByte = currentBlock[currentByte++];
		return returnByte;
	}
}

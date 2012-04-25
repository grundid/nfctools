package org.nfctools.mf.ul;

import java.io.IOException;

import org.nfctools.mf.block.MfBlock;

public interface MfUlReaderWriter {

	MfBlock[] readBlock(int startPage, int pagesToRead) throws IOException;

	void writeBlock(int startPage, MfBlock... mfBlock) throws IOException;

}
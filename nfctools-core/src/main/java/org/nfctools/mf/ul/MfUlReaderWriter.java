package org.nfctools.mf.ul;

import java.io.IOException;

import javax.smartcardio.Card;

import org.nfctools.mf.block.MfBlock;

public interface MfUlReaderWriter {

	MfBlock[] readBlock(Card card, int startPage, int pagesToRead) throws IOException;

	void writeBlock(Card card, int startPage, MfBlock... mfBlock) throws IOException;

}
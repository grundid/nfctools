/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nfctools.spi.acs;

import java.io.IOException;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import org.nfctools.mf.MfException;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.ul.DataBlock;
import org.nfctools.mf.ul.MfUlReaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class AcrMfUlReaderWriter implements MfUlReaderWriter {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public MfBlock[] readBlock(Card card, int startPage, int pagesToRead) throws IOException {
		CardChannel cardChannel = card.getBasicChannel();
		MfBlock[] returnBlocks = new MfBlock[pagesToRead];
		for (int currentPage = 0; currentPage < pagesToRead; currentPage++) {
			int pageNumber = startPage + currentPage;

			CommandAPDU readBlock = new CommandAPDU(Apdu.CLS_PTS, Apdu.INS_READ_BINARY, 0x00, pageNumber, 4);
			ResponseAPDU readBlockResponse;
			try {
				readBlockResponse = cardChannel.transmit(readBlock);
				if (!Apdu.isSuccess(readBlockResponse)) {
					throw new MfException("Reading block failed. Page: " + pageNumber + ", Response: "
							+ readBlockResponse);
				}
			}
			catch (CardException e) {
				throw new IOException(e);
			}
			returnBlocks[currentPage] = new DataBlock(readBlockResponse.getData());
		}
		return returnBlocks;
	}

	@Override
	public void writeBlock(Card card, int startPage, MfBlock... mfBlock) throws IOException {
		CardChannel cardChannel = card.getBasicChannel();
		for (int currentBlock = 0; currentBlock < mfBlock.length; currentBlock++) {
			int blockNumber = startPage + currentBlock;

			CommandAPDU writeBlock = new CommandAPDU(Apdu.CLS_PTS, Apdu.INS_UPDATE_BINARY, 0x00, blockNumber,
					mfBlock[currentBlock].getData());
			ResponseAPDU writeBlockResponse;
			try {
				writeBlockResponse = cardChannel.transmit(writeBlock);
				if (!Apdu.isSuccess(writeBlockResponse)) {
					throw new MfException("Writing block failed. Page: " + blockNumber + ", Response: "
							+ writeBlockResponse);
				}
			}
			catch (CardException e) {
				throw new IOException(e);
			}
		}
	}
}

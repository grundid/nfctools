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

import org.nfctools.api.ApduTag;
import org.nfctools.api.TagInfo;
import org.nfctools.mf.MfException;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.ul.DataBlock;
import org.nfctools.mf.ul.MfUlReaderWriter;
import org.nfctools.mf.ul.UltralightHandler;
import org.nfctools.scio.Command;
import org.nfctools.scio.Response;

public class AcrMfUlReaderWriter implements MfUlReaderWriter {

	private ApduTag tag;
	private TagInfo tagInfo;

	public AcrMfUlReaderWriter(ApduTag tag) {
		this.tag = tag;
	}

	@Override
	public MfBlock[] readBlock(int startPage, int pagesToRead) throws IOException {
		MfBlock[] returnBlocks = new MfBlock[pagesToRead];
		for (int currentPage = 0; currentPage < pagesToRead; currentPage++) {
			int pageNumber = startPage + currentPage;
			Command readBlock = new Command(Apdu.INS_READ_BINARY, 0x00, pageNumber, 4);
			Response readBlockResponse = tag.transmit(readBlock);
			if (!readBlockResponse.isSuccess()) {
				throw new MfException("Reading block failed. Page: " + pageNumber + ", Response: " + readBlockResponse);
			}
			returnBlocks[currentPage] = new DataBlock(readBlockResponse.getData());
		}
		return returnBlocks;
	}

	@Override
	public void writeBlock(int startPage, MfBlock... mfBlock) throws IOException {
		for (int currentBlock = 0; currentBlock < mfBlock.length; currentBlock++) {
			int blockNumber = startPage + currentBlock;
			Command writeBlock = new Command(Apdu.INS_UPDATE_BINARY, 0x00, blockNumber, mfBlock[currentBlock].getData());
			Response writeBlockResponse = tag.transmit(writeBlock);
			if (!writeBlockResponse.isSuccess()) {
				throw new MfException("Writing block failed. Page: " + blockNumber + ", Response: "
						+ writeBlockResponse);
			}
		}
	}

	@Override
	public TagInfo getTagInfo() throws IOException {
		if (tagInfo == null) {
			MfBlock[] idBlocks = readBlock(0, 2);
			byte[] id = UltralightHandler.extractId(idBlocks);
			tagInfo = new TagInfo(tag.getTagType(), id);
		}
		return tagInfo;
	}
}

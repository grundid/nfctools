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
package org.nfctools.spi.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.BlockResolver;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.card.MfCard1k;
import org.nfctools.mf.card.MfCard4k;
import org.nfctools.mf.classic.Key;
import org.nfctools.utils.NfcUtils;

public class FileMfReaderWriter implements MfReaderWriter {

	private Map<MfCard, Map<Integer, MfBlock>> cardBlockMap = new HashMap<MfCard, Map<Integer, MfBlock>>();
	private MfCardListener cardListener;

	public MfCard loadCardFromFile(String fileName) throws IOException {
		Collection<String> lines = readLinesFromFile(fileName);

		MfCard mfCard = lines.size() == 256 ? new MfCard4k(null, null) : lines.size() == 64 ? new MfCard1k(null, null)
				: null;
		if (mfCard == null)
			throw new MfException("unknown card. lines " + lines.size());

		BlockResolver blockResolver = new BlockResolver();

		Map<Integer, MfBlock> blockMap = new HashMap<Integer, MfBlock>();
		int blockNumber = 0;
		Pattern pattern = Pattern
				.compile("S(.*)\\|B(.*) Key: (............).*\\[(................................)\\]");

		for (String data : lines) {
			Matcher matcher = pattern.matcher(data);
			if (matcher.matches()) {
				int sectorId = Integer.parseInt(matcher.group(1));
				int blockId = Integer.parseInt(matcher.group(2));
				byte[] keyA = NfcUtils.convertASCIIToBin(matcher.group(3));
				byte[] blockData = NfcUtils.convertASCIIToBin(matcher.group(4));

				MfBlock resolvedBlock = blockResolver.resolveBlock(mfCard, sectorId, blockId, blockData);
				if (mfCard.isTrailerBlock(sectorId, blockId))
					System.arraycopy(keyA, 0, blockData, 0, 6);
				blockMap.put(blockNumber, resolvedBlock);
				blockNumber++;
			}
		}

		if (blockNumber == 0)
			throw new RuntimeException("not valid data for card");

		cardBlockMap.put(mfCard, blockMap);
		if (cardListener != null)
			cardListener.cardDetected(mfCard, this);

		return mfCard;
	}

	private Collection<String> readLinesFromFile(String fileName) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(fileName)));
		while (br.ready()) {
			lines.add(br.readLine());
		}
		return lines;
	}

	@Override
	public void setCardIntoHalt(MfCard mfCard) throws IOException {
		cardBlockMap.remove(mfCard);
	}

	@Override
	public void reselectCard(MfCard mfCard) throws IOException {
	}

	@Override
	public MfBlock[] readBlock(MfAccess mfAccess) throws IOException {
		checkReadAccess(mfAccess);
		Map<Integer, MfBlock> blockMap = cardBlockMap.get(mfAccess.getCard());
		MfBlock[] blocks = new MfBlock[mfAccess.getBlocksToRead()];
		for (int x = 0; x < mfAccess.getBlocksToRead(); x++) {
			blocks[x] = blockMap.get(mfAccess.getCard().getBlockNumber(mfAccess.getSector(), mfAccess.getBlock() + x));
		}
		return blocks;
	}

	private void checkReadAccess(MfAccess mfAccess) throws IOException {
		int trailerBlockId = mfAccess.getCard().getTrailerBlockNumberForSector(mfAccess.getSector());
		Map<Integer, MfBlock> blockMap = cardBlockMap.get(mfAccess.getCard());

		TrailerBlock trailerBlock = (TrailerBlock)blockMap.get(mfAccess.getCard().getBlockNumber(mfAccess.getSector(),
				trailerBlockId));

		byte[] key = trailerBlock.getKey(mfAccess.getKey());

		if (Key.B.equals(mfAccess.getKey()) && trailerBlock.isKeyBReadable())
			throw new MfException("Cannot login with Key B. Key B is readable");

		if (!NfcUtils.isEqualArray(key, mfAccess.getKeyValue())) {
			throw new MfLoginException("Login failed. Sector: " + mfAccess.getSector() + ", Block: "
					+ mfAccess.getBlock() + " Key: " + mfAccess.getKey().name() + " Given: "
					+ NfcUtils.convertBinToASCII(mfAccess.getKeyValue()) + ", Expected: "
					+ NfcUtils.convertBinToASCII(key));

		}
	}

	@Override
	public void writeBlock(MfAccess mfAccess, MfBlock... mfBlock) throws IOException {
		Map<Integer, MfBlock> blockMap = cardBlockMap.get(mfAccess.getCard());

		for (int currentBlock = 0; currentBlock < mfBlock.length; currentBlock++) {
			checkWriteAccess(mfAccess, mfAccess.getBlock() + currentBlock);
			int blockNumber = mfAccess.getCard().getBlockNumber(mfAccess.getSector(), mfAccess.getBlock())
					+ currentBlock;
			blockMap.put(blockNumber, mfBlock[currentBlock]);
		}
	}

	private void checkWriteAccess(MfAccess mfAccess, int blockId) throws IOException {
		int trailerBlockId = mfAccess.getCard().getTrailerBlockNumberForSector(mfAccess.getSector());
		Map<Integer, MfBlock> blockMap = cardBlockMap.get(mfAccess.getCard());

		TrailerBlock trailerBlock = (TrailerBlock)blockMap.get(mfAccess.getCard().getBlockNumber(mfAccess.getSector(),
				trailerBlockId));

		byte[] key = trailerBlock.getKey(mfAccess.getKey());

		if (Key.B.equals(mfAccess.getKey()) && trailerBlock.isKeyBReadable())
			throw new MfLoginException("Cannot login with Key B. Key B is readable. Sector: " + mfAccess.getSector()
					+ ", Block: " + blockId);

		int dataArea = (int)(mfAccess.getCard().getBlocksPerSector(mfAccess.getSector()) > 3 ? Math
				.floor((double)mfAccess.getCard().getBlocksPerSector(mfAccess.getSector()) / 5.0) : blockId);

		if ((blockId == trailerBlockId && !trailerBlock.canWriteTrailerBlock(mfAccess.getKey()))
				|| (blockId != trailerBlockId && !trailerBlock.canWriteDataBlock(mfAccess.getKey(), dataArea))) {
			throw new MfLoginException("Write Access Denied. Sector: " + mfAccess.getSector() + ", Block: " + blockId
					+ " Key: " + mfAccess.getKey().name());

		}

		if (!NfcUtils.isEqualArray(key, mfAccess.getKeyValue())) {
			throw new MfLoginException("Login failed. Sector: " + mfAccess.getSector() + ", Block: " + blockId
					+ " Key: " + mfAccess.getKey().name() + " Given: "
					+ NfcUtils.convertBinToASCII(mfAccess.getKeyValue()) + ", Expected: "
					+ NfcUtils.convertBinToASCII(key));
		}
	}

	@Override
	public void setCardListener(MfCardListener mfCardListener) throws IOException {
		this.cardListener = mfCardListener;
	}

	@Override
	public boolean waitForCard(MfCardListener mfCardListener, int timeout) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeCardListener() {
		this.cardListener = null;
	}

}

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
package org.nfctools.spi.arygon;

import java.io.IOException;

import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.Block;
import org.nfctools.mf.block.BlockResolver;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArygonReaderWriter implements MfReaderWriter {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ArygonHighLevelReaderWriter nfcReaderWriter;
	private Thread pollingThread = null;
	private CardResolver cardResolver = new CardResolver();
	private BlockResolver mfBlockResolver = new BlockResolver();

	public ArygonReaderWriter(ArygonHighLevelReaderWriter nfcReaderWriter) {
		this.nfcReaderWriter = nfcReaderWriter;
	}

	@Override
	public MfBlock[] readBlock(MfAccess mfAccess) throws IOException {
		loginIfNotAuthenticated(mfAccess);
		MfBlock[] returnBlocks = new Block[mfAccess.getBlocksToRead()];
		for (int block = 0; block < mfAccess.getBlocksToRead(); block++) {
			String blockNumber = createBlockNumber(mfAccess, block);
			nfcReaderWriter.sendMessage(("0r" + blockNumber).getBytes());
			ArygonMessage message = nfcReaderWriter.receiveMessage();
			if (message.hasTamaErrorCode())
				throwException(mfAccess, message, "Cannot read.");

			byte[] rawData = new byte[message.getPayload().length - 4];
			System.arraycopy(message.getPayload(), 4, rawData, 0, message.getPayload().length - 4);
			returnBlocks[block] = mfBlockResolver.resolveBlock(mfAccess.getCard(), mfAccess.getSector(), block
					+ mfAccess.getBlock(), rawData);
		}
		return returnBlocks;
	}

	private void throwException(MfAccess mfAccess, ArygonMessage message, String exceptionMessage) throws IOException {
		if (message.getTamaErrorCode() == 0x13) {
			reselectCard(mfAccess.getCard());
			throw new MfLoginException("Cannot login.");
		}
		else
			throw new MfException(exceptionMessage + " Sector: " + mfAccess.getSector() + ", Block: "
					+ mfAccess.getBlock() + " Key: " + mfAccess.getKey().name() + ", Tama: "
					+ new String(message.getPayload()) + " TamaCode: " + message.getTamaErrorCode());
	}

	private void loginIfNotAuthenticated(MfAccess mfAccess) throws IOException {
		loginIntoSector(mfAccess);
	}

	private void loginIntoSector(MfAccess mfAccess) throws IOException {
		String blockNumber = createBlockNumber(mfAccess, 0);
		String loginMessage = "0l" + blockNumber + "ff" + mfAccess.getKey().name()
				+ NfcUtils.convertBinToASCII(mfAccess.getKeyValue());
		nfcReaderWriter.sendMessage(loginMessage.getBytes());
		ArygonMessage message = nfcReaderWriter.receiveMessage();
		if (message.hasTamaErrorCode()) {
			reselectCard(mfAccess.getCard());
			throw new MfLoginException("Login failed. Sector: " + mfAccess.getSector() + ", Block: "
					+ mfAccess.getBlock() + " Key: " + mfAccess.getKey().name() + ", Tama: "
					+ new String(message.getPayload()));
		}
	}

	@Override
	public void writeBlock(MfAccess mfAccess, MfBlock... mfBlock) throws IOException {
		loginIfNotAuthenticated(mfAccess);
		for (int x = 0; x < mfBlock.length; x++) {
			String blockNumber = createBlockNumber(mfAccess, x);

			if (mfAccess.getCard().isTrailerBlock(mfAccess.getSector(), mfAccess.getBlock() + x)) {
				if (!(mfBlock[x] instanceof TrailerBlock))
					throw new MfException("invalid block for trailer");
			}
			nfcReaderWriter.sendMessage(("0wb" + blockNumber + NfcUtils.convertBinToASCII(mfBlock[x].getData()))
					.getBytes());
			ArygonMessage message = nfcReaderWriter.receiveMessage();
			if (message.hasTamaErrorCode())
				throwException(mfAccess, message, "Cannot write.");
		}
	}

	private String createBlockNumber(MfAccess mfAccess, int blockOffset) {
		int blockNumber = mfAccess.getCard().getBlockNumber(mfAccess.getSector(), mfAccess.getBlock()) + blockOffset;
		String rb = Integer.toHexString(blockNumber).toUpperCase();
		if (rb.length() == 1)
			rb = "0" + rb;
		return rb;
	}

	@Override
	public void setCardIntoHalt(MfCard card) throws IOException {
		String targetId = "00";
		log.debug("Halting Card " + card.getId() + " / TargetId: " + targetId);
		nfcReaderWriter.sendMessage(("0h" + targetId).getBytes());
		ArygonMessage message = nfcReaderWriter.receiveMessage();
		if (message.hasTamaErrorCode())
			throw new MfException("Cannot send data.");
	}

	public void scanForCard() throws IOException {
		nfcReaderWriter.sendMessage("0s".getBytes());
	}

	public MfCard readCard() throws IOException {
		ArygonMessage message = nfcReaderWriter.receiveMessage();
		if (message.hasPayload()) {
			byte[] data = NfcUtils.convertHexAsciiToByteArray(message.getPayload());
			byte targetNumber = data[2];
			int type = data[5];
			byte nfcIdLength = data[6];
			byte[] cardId = new byte[nfcIdLength];
			System.arraycopy(data, 7, cardId, 0, nfcIdLength);

			return cardResolver.resolvecard(type, cardId, targetNumber);
		}
		else
			throw new MfException("no card");
	}

	@Override
	public void reselectCard(MfCard card) throws IOException {
		scanForCard();
		MfCard newcard = readCard();
		if (!NfcUtils.isEqualArray(card.getId(), newcard.getId()))
			throw new MfException("New card detected. Id does not match. (Expected: " + card + ", got: " + newcard
					+ ")");
	}

	@Override
	public void setCardListener(MfCardListener cardListener) throws IOException {
		scanForCard();
		pollingThread = new Thread(new PollingCardScanner(nfcReaderWriter, cardListener, this));
		pollingThread.setDaemon(false);
		log.debug("Starting new thread " + pollingThread.getName());
		pollingThread.start();
	}

	@Override
	public boolean waitForCard(MfCardListener mfCardListener, int timeout) throws IOException {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public void removeCardListener() {
		if (pollingThread != null && pollingThread.isAlive()) {
			pollingThread.interrupt();
		}
	}
}

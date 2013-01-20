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

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import org.nfctools.io.NfcDevice;
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
import org.nfctools.mf.classic.Key;
import org.nfctools.scio.CardTerminalToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AcsReaderWriter implements MfReaderWriter {

	protected Logger log = LoggerFactory.getLogger(getClass());
	protected BlockResolver blockResolver = new BlockResolver();

	protected CardTerminal cardTerminal;
	private Thread pollingThread = null;

	protected AcsReaderWriter(NfcDevice nfcDevice) {
		if (nfcDevice.getConnectionToken() instanceof CardTerminalToken)
			this.cardTerminal = ((CardTerminalToken)nfcDevice.getConnectionToken()).getCardTerminal();
		else
			throw new IllegalArgumentException("unsupported connection token");
	}

	@Override
	public void setCardIntoHalt(MfCard card) throws IOException {
		try {
			AcsConnectionToken connectionToken = (AcsConnectionToken)card.getConnectionToken();
			connectionToken.getCard().disconnect(true);
		}
		catch (CardException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void reselectCard(MfCard card) throws IOException {
	}

	@Override
	public MfBlock[] readBlock(MfAccess mfAccess) throws IOException {

		AcsConnectionToken connectionToken = retrieveConnectionToken(mfAccess);
		CardChannel cardChannel = connectionToken.getCard().getBasicChannel();

		byte blockNumber;
		loginIntoSector(mfAccess, cardChannel);

		MfBlock[] returnBlocks = new Block[mfAccess.getBlocksToRead()];

		for (int currentBlock = 0; currentBlock < mfAccess.getBlocksToRead(); currentBlock++) {

			blockNumber = (byte)mfAccess.getCard().getBlockNumber(mfAccess.getSector(),
					mfAccess.getBlock() + currentBlock);

			CommandAPDU readBlock = new CommandAPDU(Apdu.CLS_PTS, Apdu.INS_READ_BINARY, 0x00, blockNumber, 16);
			ResponseAPDU readBlockResponse;
			try {
				readBlockResponse = cardChannel.transmit(readBlock);
				if (!ApduUtils.isSuccess(readBlockResponse)) {
					throw new MfException("Reading block failed. Sector: " + mfAccess.getSector() + ", Block: "
							+ mfAccess.getBlock() + " Key: " + mfAccess.getKey().name() + ", Response: "
							+ readBlockResponse);
				}

			}
			catch (CardException e) {
				throw new IOException(e);
			}

			returnBlocks[currentBlock] = blockResolver.resolveBlock(mfAccess.getCard(), mfAccess.getSector(),
					currentBlock + mfAccess.getBlock(), readBlockResponse.getData());

		}
		return returnBlocks;
	}

	protected abstract void loginIntoSector(MfAccess mfAccess, CardChannel cardChannel) throws IOException;

	protected void loginIntoSector(MfAccess mfAccess, CardChannel cardChannel, byte memoryKeyId) throws IOException {
		try {
			CommandAPDU loadKey = new CommandAPDU(Apdu.CLS_PTS, Apdu.INS_EXTERNAL_AUTHENTICATE,
					Acs.P1_LOAD_KEY_INTO_VOLATILE_MEM, memoryKeyId, mfAccess.getKeyValue());
			ResponseAPDU loadKeyResponse = cardChannel.transmit(loadKey);
			if (!ApduUtils.isSuccess(loadKeyResponse)) {
				throw new MfLoginException("Loading key failed. Sector: " + mfAccess.getSector() + ", Block: "
						+ mfAccess.getBlock() + " Key: " + mfAccess.getKey().name() + ", Response: " + loadKeyResponse);
			}

			byte blockNumber = (byte)mfAccess.getCard().getBlockNumber(mfAccess.getSector(), mfAccess.getBlock());

			byte keyTypeToUse = mfAccess.getKey() == Key.A ? Acs.KEY_A : Acs.KEY_B;

			CommandAPDU auth = new CommandAPDU(Apdu.CLS_PTS, Apdu.INS_INTERNAL_AUTHENTICATE_ACS, 0, 0, new byte[] {
					0x01, 0x00, blockNumber, keyTypeToUse, memoryKeyId });
			ResponseAPDU authResponse = cardChannel.transmit(auth);
			if (!ApduUtils.isSuccess(authResponse)) {
				throw new MfLoginException("Login failed. Sector: " + mfAccess.getSector() + ", Block: "
						+ mfAccess.getBlock() + " Key: " + mfAccess.getKey().name() + ", Response: " + authResponse);
			}
		}
		catch (CardException e) {
			throw new IOException(e);
		}
	}

	private AcsConnectionToken retrieveConnectionToken(MfAccess mfAccess) {
		return (AcsConnectionToken)mfAccess.getCard().getConnectionToken();
	}

	@Override
	public void writeBlock(MfAccess mfAccess, MfBlock... mfBlock) throws IOException {
		AcsConnectionToken connectionToken = retrieveConnectionToken(mfAccess);
		CardChannel cardChannel = connectionToken.getCard().getBasicChannel();

		loginIntoSector(mfAccess, cardChannel);

		for (int currentBlock = 0; currentBlock < mfBlock.length; currentBlock++) {
			int blockNumber = mfAccess.getCard().getBlockNumber(mfAccess.getSector(), mfAccess.getBlock())
					+ currentBlock;

			if (mfAccess.getCard().isTrailerBlock(mfAccess.getSector(), mfAccess.getBlock() + currentBlock)) {
				if (!(mfBlock[currentBlock] instanceof TrailerBlock))
					throw new MfException("invalid block for trailer");
			}

			CommandAPDU writeBlock = new CommandAPDU(Apdu.CLS_PTS, Apdu.INS_UPDATE_BINARY, 0x00, blockNumber,
					mfBlock[currentBlock].getData());
			ResponseAPDU writeBlockResponse;
			try {
				writeBlockResponse = cardChannel.transmit(writeBlock);
				if (!ApduUtils.isSuccess(writeBlockResponse)) {
					throw new MfException("Writing block failed. Sector: " + mfAccess.getSector() + ", Block: "
							+ mfAccess.getBlock() + " Key: " + mfAccess.getKey().name() + ", Response: "
							+ writeBlockResponse);
				}

			}
			catch (CardException e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	public void setCardListener(MfCardListener cardListener) throws IOException {
		pollingThread = new Thread(new PollingCardScanner(cardTerminal, cardListener, this));
		pollingThread.setDaemon(false);
		log.debug("Starting new thread " + pollingThread.getName());
		pollingThread.start();
	}

	@Override
	public boolean waitForCard(MfCardListener mfCardListener, int timeout) throws IOException {
		removeCardListener();
		try {
			return new PollingCardScanner(cardTerminal, mfCardListener, this).waitForCard(timeout);
		}
		catch (CardException e) {
			throw new IOException(e);
		}
		catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void removeCardListener() {
		if (pollingThread != null && pollingThread.isAlive()) {
			pollingThread.interrupt();
		}
	}
}

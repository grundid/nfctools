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
package org.nfctools.mf.mad;

import java.io.IOException;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.NxpCrc;
import org.nfctools.mf.block.DataBlock;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.classic.KeyValue;
import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.classic.MfClassicAccess;
import org.nfctools.mf.classic.MfClassicConstants;
import org.nfctools.mf.classic.MfClassicReaderWriter;

public abstract class AbstractMad implements ApplicationDirectory {

	protected static final byte[] FREE_SLOT = { 0x00, 0x00 };

	protected MadKeyConfig keyConfig;
	protected MemoryLayout memoryLayout;
	protected MfClassicReaderWriter readerWriter;
	private boolean readonly;

	protected class Space {

		int firstSlot;
		int lastSlot;
		int continousSize;
	}

	public AbstractMad(MfClassicReaderWriter readerWriter, MadKeyConfig keyConfig) {
		if (keyConfig == null)
			throw new IllegalArgumentException("keyConfig cannot be null");
		this.readerWriter = readerWriter;
		this.keyConfig = keyConfig;
		this.memoryLayout = readerWriter.getMemoryLayout();
	}

	protected void readMad(byte[] madData, int sectorId, int firstBlockId, TrailerBlock trailerBlock)
			throws IOException {
		int blocksToRead = madData.length / MfConstants.BYTES_PER_BLOCK;
		MfClassicAccess access = new MfClassicAccess(new KeyValue(Key.A, trailerBlock.getKey(Key.A)), sectorId,
				firstBlockId, blocksToRead);
		MfBlock[] madBlocks = readerWriter.readBlock(access);
		for (int x = 0; x < blocksToRead; x++) {
			System.arraycopy(madBlocks[x].getData(), 0, madData, x * MfConstants.BYTES_PER_BLOCK,
					MfConstants.BYTES_PER_BLOCK);
		}
	}

	protected void writeMad(byte[] madData, int sectorId, int firstBlockId, TrailerBlock trailerBlock)
			throws IOException {
		int blocksToWrite = madData.length / MfConstants.BYTES_PER_BLOCK;
		DataBlock[] dataBlocks = new DataBlock[blocksToWrite];

		for (int x = 0; x < blocksToWrite; x++) {
			byte[] writeBuffer = new byte[MfConstants.BYTES_PER_BLOCK];
			System.arraycopy(madData, x * MfConstants.BYTES_PER_BLOCK, writeBuffer, 0, MfConstants.BYTES_PER_BLOCK);
			dataBlocks[x] = new DataBlock(writeBuffer);
		}

		MfClassicAccess access = new MfClassicAccess(new KeyValue(Key.B, trailerBlock.getKey(Key.B)), sectorId,
				firstBlockId);
		readerWriter.writeBlock(access, dataBlocks);
	}

	protected byte createCrc(byte[] madData) {
		if (isReadonly())
			throw new IllegalStateException("cannot modify readonly mad");

		NxpCrc crc = new NxpCrc();

		for (int x = 1; x < madData.length; x++) {
			crc.add(madData[x]);
		}

		return crc.getCrc();
	}

	protected void writeTrailer(int trailerSectorId, TrailerBlock trailerBlock) throws IOException {
		MfClassicAccess accessTrailer = new MfClassicAccess(new KeyValue(keyConfig.getCreateKey(),
				keyConfig.getCreateKeyValue()), trailerSectorId,
				memoryLayout.getTrailerBlockNumberForSector(trailerSectorId));
		readerWriter.writeBlock(accessTrailer, trailerBlock);
	}

	public void setReadonly() {
		this.readonly = true;
	}

	/**
	 * Returns the sector number (starting with 0) on the card for the given slot.
	 * 
	 * @param slot
	 */
	protected abstract int getSectorIdForSlot(int slot);

	/**
	 * Returns the amount of bytes in the given slot.
	 * 
	 * @param slot
	 */
	protected abstract int getSlotSize(int slot);

	protected abstract void setAid(int aidSlot, byte[] aid);

	protected abstract byte[] getAid(int aidSlot);

	protected abstract void readMad() throws IOException;

	protected abstract void writeMad() throws IOException;

	protected Space getMaxContinousSpaceForAid(byte[] aid) {
		Space maxContinousSpace = new Space();

		int localMax = 0;
		int possibleFirstSlot = 0;
		for (int slot = 0; slot < getNumberOfSlots(); slot++) {
			byte[] slotAid = getAid(slot);
			if (slotAid[0] == aid[0] && slotAid[1] == aid[1]) {
				localMax += getSlotSize(slot);
				maxContinousSpace.firstSlot = possibleFirstSlot;
				maxContinousSpace.continousSize = Math.max(localMax, maxContinousSpace.continousSize);
				maxContinousSpace.lastSlot = slot;
			}
			else {
				possibleFirstSlot = slot + 1;
				localMax = 0;
			}
		}

		return maxContinousSpace;
	}

	@Override
	public Application openApplication(ApplicationId aId) {
		Space space = getMaxContinousSpaceForAid(aId.getAid());
		if (space.continousSize > 0)
			return new ApplicationImpl(aId, space.continousSize, readerWriter, space.firstSlot, space.lastSlot, this);
		else
			throw new IllegalArgumentException("aid not available");
	}

	@Override
	public void deleteApplication(ApplicationId aId, byte[] writeKeyValue, TrailerBlock trailerBlock)
			throws IOException {
		if (isReadonly())
			throw new IllegalStateException("cannot modify readonly mad");

		Space space = getMaxContinousSpaceForAid(aId.getAid());

		for (int slot = space.firstSlot; slot <= space.lastSlot; slot++) {

			int sectorId = getSectorIdForSlot(slot);

			writeBlock(Key.B, writeKeyValue, trailerBlock, sectorId);
			setAid(slot, FREE_SLOT);
		}

		writeMad();
	}

	@Override
	public Application createApplication(ApplicationId aId, int sizeToAllocate, byte[] writeKeyValue,
			TrailerBlock trailerBlock) throws IOException {
		if (isReadonly())
			throw new IllegalStateException("cannot modify readonly mad");

		if (sizeToAllocate <= 0)
			throw new IllegalArgumentException("cannot create an empty application");

		Space space = getMaxContinousSpaceForAid(FREE_SLOT);
		if (sizeToAllocate <= space.continousSize) {

			int allocatedSize = 0;
			int slot = space.firstSlot;
			int lastSlotAllocated = slot;
			while (allocatedSize < sizeToAllocate && slot <= space.lastSlot) {

				int sectorId = getSectorIdForSlot(slot);

				try {
					writeBlock(Key.B, writeKeyValue, trailerBlock, sectorId);
				}
				catch (MfLoginException e) {
					writeBlock(Key.A, MfConstants.TRANSPORT_KEY, trailerBlock, sectorId);
				}
				setAid(slot, aId.getAid());
				allocatedSize += getSlotSize(slot);
				lastSlotAllocated = slot;
				slot++;
			}

			writeMad();
			return new ApplicationImpl(aId, allocatedSize, readerWriter, space.firstSlot, lastSlotAllocated, this);
		}
		else
			throw new IllegalArgumentException("not enough space (" + space.continousSize + ")");
	}

	private void writeBlock(Key writeKey, byte[] writeKeyValue, TrailerBlock trailerBlock, int sectorId)
			throws IOException {

		MfClassicAccess access = new MfClassicAccess(new KeyValue(writeKey, writeKeyValue), sectorId,
				memoryLayout.getTrailerBlockNumberForSector(sectorId));

		readerWriter.writeBlock(access, trailerBlock);
	}

	@Override
	public boolean isReadonly() {
		return readonly;
	}

	@Override
	public boolean hasApplication(ApplicationId aId) throws IOException {
		Space space = getMaxContinousSpaceForAid(aId.getAid());
		return (space.continousSize > 0);
	}

	public static ApplicationDirectory initInstance(MfClassicReaderWriter readerWriter, MadKeyConfig keyConfig)
			throws IOException {
		byte[] writeKeyValue = keyConfig == null ? null : keyConfig.getWriteKeyValue();
		MemoryLayout memoryLayout = readerWriter.getMemoryLayout();
		MfClassicAccess accessTrailer = new MfClassicAccess(MfClassicConstants.MAD_KEY, 0,
				memoryLayout.getTrailerBlockNumberForSector(0));
		TrailerBlock madTrailer = (TrailerBlock)readerWriter.readBlock(accessTrailer)[0];

		if ((madTrailer.getGeneralPurposeByte() & MadConstants.GPB_MAD_AVAILABLE) != 0) {
			if ((madTrailer.getGeneralPurposeByte() & MadConstants.GPB_MAD_V1) == MadConstants.GPB_MAD_V1) {
				madTrailer.setKey(Key.A, MadConstants.DEFAULT_MAD_KEY);
				if (writeKeyValue != null)
					madTrailer.setKey(Key.B, writeKeyValue);
				Mad1 mad1 = new Mad1(readerWriter, keyConfig, madTrailer);
				mad1.readMad();
				if (writeKeyValue == null)
					mad1.setReadonly();
				return mad1;
			}
			else if ((madTrailer.getGeneralPurposeByte() & MadConstants.GPB_MAD_V2) == MadConstants.GPB_MAD_V2) {
				madTrailer.setKey(Key.A, MadConstants.DEFAULT_MAD_KEY);
				if (writeKeyValue != null)
					madTrailer.setKey(Key.B, writeKeyValue);
				Mad2 mad = new Mad2(readerWriter, keyConfig, madTrailer);
				mad.readMad();
				if (writeKeyValue == null)
					mad.setReadonly();
				return mad;
			}
			else {
				throw new MfException("MAD version not supported");
			}
		}
		else {
			throw new MfException("MAD not available");
		}
	}

	public static ApplicationDirectory createInstance(MfClassicReaderWriter readerWriter, MadKeyConfig keyConfig)
			throws IOException {
		MemoryLayout memoryLayout = readerWriter.getMemoryLayout();
		if (memoryLayout.getMadVersion() == MadConstants.GPB_MAD_V1) {
			Mad1 mad1 = new Mad1(readerWriter, keyConfig);
			mad1.initMadTrailer(memoryLayout.getMadVersion());
			mad1.writeMad();
			return mad1;
		}

		if (memoryLayout.getMadVersion() == MadConstants.GPB_MAD_V2) {
			Mad2 mad2 = new Mad2(readerWriter, keyConfig);
			mad2.initMadTrailer(memoryLayout.getMadVersion());
			mad2.writeMad();
			return mad2;
		}
		throw new RuntimeException("Unsupported MAD version" + memoryLayout.getMadVersion());
	}
}

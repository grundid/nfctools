package org.nfctools.mf.mad;

import java.io.IOException;

import org.nfctools.mf.Key;
import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.NxpCrc;
import org.nfctools.mf.block.DataBlock;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;

public abstract class AbstractMad implements ApplicationDirectory {

	protected static final byte[] FREE_SLOT = { 0x00, 0x00 };

	protected MfReaderWriter readerWriter;
	protected MfCard card;
	private boolean readonly;

	protected class Space {

		int firstSlot;
		int lastSlot;
		int continousSize;
	}

	protected void readMad(byte[] madData, int sectorId, int firstBlockId, TrailerBlock trailerBlock)
			throws IOException {
		int blocksToRead = madData.length / MfConstants.BYTES_PER_BLOCK;
		MfBlock[] madBlocks = readerWriter.readBlock(new MfAccess(card, sectorId, firstBlockId, blocksToRead, Key.A,
				trailerBlock.getKey(Key.A)));
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

		MfAccess mfAccess = new MfAccess(card, sectorId, firstBlockId, Key.B, trailerBlock.getKey(Key.B));
		readerWriter.writeBlock(mfAccess, dataBlocks);
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

	void setReadonly(boolean readonly) {
		this.readonly = readonly;
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
			return new ApplicationImpl(aId, space.continousSize, readerWriter, card, space.firstSlot, space.lastSlot,
					this);
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
			return new ApplicationImpl(aId, allocatedSize, readerWriter, card, space.firstSlot, lastSlotAllocated, this);
		}
		else
			throw new IllegalArgumentException("not enough space (" + space.continousSize + ")");
	}

	private void writeBlock(Key writeKey, byte[] writeKeyValue, TrailerBlock trailerBlock, int sectorId)
			throws IOException {
		MfAccess mfAccess = new MfAccess(card, sectorId, card.getTrailerBlockNumberForSector(sectorId), writeKey,
				writeKeyValue);

		readerWriter.writeBlock(mfAccess, trailerBlock);
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

}

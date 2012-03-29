package org.nfctools.mf.mad;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.nfctools.mf.Key;
import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.DataBlock;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;

public class ApplicationImpl implements Application {

	private ApplicationId applicationId;
	private MfReaderWriter readerWriter;
	private MfCard card;
	private int firstSlot;
	private int lastSlot;
	private int allocatedSize;

	private AbstractMad mad;

	public ApplicationImpl(ApplicationId applicationId, int allocatedSize, MfReaderWriter readerWriter, MfCard card,
			int firstSlot, int lastSlot, AbstractMad mad) {
		this.applicationId = applicationId;
		this.allocatedSize = allocatedSize;
		this.readerWriter = readerWriter;
		this.card = card;
		this.firstSlot = firstSlot;
		this.lastSlot = lastSlot;
		this.mad = mad;
	}

	@Override
	public byte[] getApplicationId() {
		return applicationId.getAid();
	}

	@Override
	public int getAllocatedSize() {
		return allocatedSize;
	}

	@Override
	public byte[] read(Key key, byte[] keyValue) throws IOException {
		// TODO create SlotIterator
		ByteArrayOutputStream baos = new ByteArrayOutputStream(allocatedSize);
		for (int slot = firstSlot; slot <= lastSlot; slot++) {
			int sectorId = mad.getSectorIdForSlot(slot);
			try {
				readBlockData(key, keyValue, baos, sectorId);
			}
			catch (MfLoginException e) {
				readBlockData(key, MfConstants.TRANSPORT_KEY, baos, sectorId);
			}
		}
		return baos.toByteArray();
	}

	private void readBlockData(Key key, byte[] keyValue, ByteArrayOutputStream baos, int sectorId) throws IOException {
		MfAccess mfAccess = new MfAccess(card, sectorId, 0, card.getDataBlocksPerSector(sectorId), key, keyValue);
		MfBlock[] blocks = readerWriter.readBlock(mfAccess);
		for (MfBlock block : blocks) {
			baos.write(block.getData());
		}
	}

	@Override
	public void write(Key key, byte[] keyValue, byte[] content) throws IOException {

		if (content.length <= getAllocatedSize()) {

			ByteArrayInputStream bais = new ByteArrayInputStream(content);

			for (int slot = firstSlot; slot <= lastSlot; slot++) {
				int sectorId = mad.getSectorIdForSlot(slot);
				for (int blockId = 0; blockId < card.getDataBlocksPerSector(sectorId); blockId++) {
					MfAccess mfAccess = new MfAccess(card, sectorId, blockId, key, keyValue);

					byte[] buffer = new byte[MfConstants.BYTES_PER_BLOCK];
					if (bais.available() > 0)
						bais.read(buffer);

					DataBlock dataBlock = new DataBlock(buffer);
					readerWriter.writeBlock(mfAccess, dataBlock);
				}
			}
		}
		else {
			throw new IllegalArgumentException("content length too big for allocated space");
		}
	}

	@Override
	public void updateTrailer(Key key, byte[] keyValue, TrailerBlock trailerBlock) throws IOException {
		for (int slot = firstSlot; slot <= lastSlot; slot++) {
			int sectorId = mad.getSectorIdForSlot(slot);
			MfAccess mfAccess = new MfAccess(card, sectorId, card.getTrailerBlockNumberForSector(sectorId), key,
					keyValue);
			readerWriter.writeBlock(mfAccess, trailerBlock);
		}
	}

}

package org.nfctools.mf.mad;

import java.io.IOException;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;

public class Mad1 extends AbstractMad {

	protected byte[] madData = new byte[32];

	protected TrailerBlock trailerBlock;
	private final int sectorId = 0;

	Mad1(MfReaderWriter readerWriter, MfCard mfCard, TrailerBlock trailerBlock) throws IOException {
		this.readerWriter = readerWriter;
		this.card = mfCard;
		this.trailerBlock = trailerBlock;
	}

	@Override
	protected void readMad() throws IOException {
		readMad(madData, sectorId, 1, trailerBlock);
	}

	@Override
	protected void writeMad() throws IOException {
		updateCrc();
		writeMad(madData, sectorId, 1, trailerBlock);
	}

	protected void updateCrc() {
		if (isReadonly())
			throw new IllegalStateException("cannot modify readonly mad");
		madData[0] = createCrc(madData);
	}

	@Override
	protected byte[] getAid(int aidSlot) {
		if (aidSlot < 0 || aidSlot >= getNumberOfSlots())
			throw new IllegalArgumentException("aid slot out of range");
		return new byte[] { madData[aidSlot * 2 + 2], madData[aidSlot * 2 + 3] };
	}

	@Override
	protected void setAid(int aidSlot, byte[] aid) {
		if (aidSlot < 0 || aidSlot >= getNumberOfSlots())
			throw new IllegalArgumentException("aid slot out of range");
		madData[aidSlot * 2 + 2] = aid[0];
		madData[aidSlot * 2 + 3] = aid[1];
		updateCrc();
	}

	@Override
	protected int getSectorIdForSlot(int slot) {
		return slot + 1;
	}

	@Override
	protected int getSlotSize(int aidSlot) {
		if (aidSlot < 0 || aidSlot >= getNumberOfSlots())
			throw new IllegalArgumentException("aid slot out of range");
		return MfConstants.BYTES_PER_BLOCK * 3;
	}

	@Override
	public int getGeneralPurposeByte() {
		return trailerBlock.getGeneralPurposeByte();
	}

	@Override
	public int getInfoByte() {
		return madData[1];
	}

	@Override
	public int getMaxContinousSize() {
		return getMaxContinousSpaceForAid(FREE_SLOT).continousSize;
	}

	@Override
	public int getVersion() {
		return getGeneralPurposeByte() & 0x03;
	}

	@Override
	public boolean isFree(int aidSlot) {
		byte[] aid = getAid(aidSlot);
		return aid[0] == 0x00 && aid[1] == 0x00;
	}

	@Override
	public int getNumberOfSlots() {
		return 15;
	}
}

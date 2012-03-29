package org.nfctools.mf.mad;

import java.io.IOException;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;

public class Mad2 extends Mad1 {

	protected byte[] mad2Data = new byte[48];

	private final int sectorId2 = 0x10;

	Mad2(MfReaderWriter readerWriter, MfCard mfCard, TrailerBlock trailerBlock) throws IOException {
		super(readerWriter, mfCard, trailerBlock);
	}

	@Override
	protected void updateCrc() {
		super.updateCrc();
		this.mad2Data[0] = createCrc(this.mad2Data);
	}

	@Override
	protected void readMad() throws IOException {
		super.readMad();
		readMad(mad2Data, sectorId2, 0, trailerBlock);
	}

	@Override
	protected void writeMad() throws IOException {
		super.writeMad();
		writeMad(mad2Data, sectorId2, 0, trailerBlock);
	}

	@Override
	protected byte[] getAid(int aidSlot) {
		if (aidSlot < super.getNumberOfSlots())
			return super.getAid(aidSlot);
		else {
			if (aidSlot < 0 || aidSlot >= getNumberOfSlots())
				throw new IllegalArgumentException("aid slot out of range");
			return new byte[] { mad2Data[(aidSlot - 15) * 2 + 2], mad2Data[(aidSlot - 15) * 2 + 3] };
		}
	}

	@Override
	protected void setAid(int aidSlot, byte[] aid) {
		if (aidSlot < super.getNumberOfSlots())
			super.setAid(aidSlot, aid);
		else {
			if (aidSlot < 0 || aidSlot >= getNumberOfSlots())
				throw new IllegalArgumentException("aid slot out of range");
			mad2Data[(aidSlot - 15) * 2 + 2] = aid[0];
			mad2Data[(aidSlot - 15) * 2 + 3] = aid[1];
			updateCrc();
		}
	}

	@Override
	public int getNumberOfSlots() {
		return 38;
	}

	@Override
	protected int getSlotSize(int aidSlot) {
		if (aidSlot < 0 || aidSlot >= getNumberOfSlots())
			throw new IllegalArgumentException("aid slot out of range");

		if (aidSlot < 30)
			return super.getSlotSize(aidSlot);
		else {
			return MfConstants.BYTES_PER_BLOCK * 15;
		}
	}

	@Override
	protected int getSectorIdForSlot(int slot) {
		if (slot < 15)
			return super.getSectorIdForSlot(slot);
		else
			return slot + 2;
	}
}

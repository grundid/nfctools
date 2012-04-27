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
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.classic.KeyValue;
import org.nfctools.mf.classic.MfClassicAccess;
import org.nfctools.mf.classic.MfClassicReaderWriter;

public class Mad2 extends Mad1 {

	protected byte[] mad2Data = new byte[48];

	private TrailerBlock trailerBlock;
	private final int mad2sectorId = 0x10;

	Mad2(MfClassicReaderWriter readerWriter, MadKeyConfig keyConfig) throws IOException {
		super(readerWriter, keyConfig);
	}

	Mad2(MfClassicReaderWriter readerWriter, MadKeyConfig keyConfig, TrailerBlock trailerBlock) throws IOException {
		super(readerWriter, keyConfig, trailerBlock);
		this.trailerBlock = trailerBlock.clone();
		this.trailerBlock.setGeneralPurposeByte(MadConstants.GPB_MAD_2_TRAILER);
	}

	@Override
	public void initMadTrailer(int madVersion) throws MfException, IOException {
		super.initMadTrailer(madVersion);
		trailerBlock = createTrailer(keyConfig.getWriteKeyValue());
		trailerBlock.setGeneralPurposeByte(MadConstants.GPB_MAD_2_TRAILER);

		MfClassicAccess accessTrailer = new MfClassicAccess(new KeyValue(keyConfig.getCreateKey(),
				keyConfig.getCreateKeyValue()), 16, memoryLayout.getTrailerBlockNumberForSector(16));
		readerWriter.writeBlock(accessTrailer, trailerBlock);
	}

	@Override
	public void makeReadOnly() throws IOException {
		super.makeReadOnly();
		trailerBlock.setAccessConditions(MfConstants.NDEF_READ_ONLY_ACCESS_CONDITIONS);
		writeTrailer(mad2sectorId, trailerBlock);
	}

	@Override
	protected void updateCrc() {
		super.updateCrc();
		this.mad2Data[0] = createCrc(this.mad2Data);
	}

	@Override
	public void readMad() throws IOException {
		super.readMad();
		readMad(mad2Data, mad2sectorId, 0, trailerBlock);
	}

	@Override
	public void writeMad() throws IOException {
		super.writeMad();
		writeMad(mad2Data, mad2sectorId, 0, trailerBlock);
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

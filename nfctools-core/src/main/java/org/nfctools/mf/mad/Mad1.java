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
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.classic.MfClassicReaderWriter;

public class Mad1 extends AbstractMad {

	protected byte[] madData = new byte[32];

	private TrailerBlock trailerBlock;
	private final int mad1SectorId = 0;

	Mad1(MfClassicReaderWriter readerWriter, MadKeyConfig keyConfig) throws IOException {
		super(readerWriter, keyConfig);
	}

	Mad1(MfClassicReaderWriter readerWriter, MadKeyConfig keyConfig, TrailerBlock trailerBlock) throws IOException {
		super(readerWriter, keyConfig);
		this.trailerBlock = trailerBlock;
	}

	public void initMadTrailer(int madVersion) throws IOException {
		trailerBlock = createTrailer(keyConfig.getWriteKeyValue());
		trailerBlock.setGeneralPurposeByte((byte)(MadConstants.GPB_NDEF_CONFIG | madVersion));
		writeTrailer(mad1SectorId, trailerBlock);
	}

	protected TrailerBlock createTrailer(byte[] writeKeyValue) throws MfException {
		TrailerBlock trailerBlock = new TrailerBlock();
		trailerBlock.setKey(Key.A, MadConstants.DEFAULT_MAD_KEY);
		trailerBlock.setKey(Key.B, writeKeyValue);
		trailerBlock.setAccessConditions(MadConstants.READ_WRITE_ACCESS_CONDITIONS);
		return trailerBlock;
	}

	@Override
	public void readMad() throws IOException {
		readMad(madData, mad1SectorId, 1, trailerBlock);
	}

	@Override
	public void writeMad() throws IOException {
		updateCrc();
		writeMad(madData, mad1SectorId, 1, trailerBlock);
	}

	@Override
	public void makeReadOnly() throws IOException {
		trailerBlock.setAccessConditions(MfConstants.NDEF_READ_ONLY_ACCESS_CONDITIONS);
		writeTrailer(mad1SectorId, trailerBlock);
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

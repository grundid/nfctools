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
package org.nfctools.mf.classic;

import org.nfctools.mf.mad.MadConstants;

public class MemoryLayout {

	private static final int FOUR_BLOCK_SECTORS = 32;
	private static final int SIXTEEN_BLOCK_SECTORS = 8;

	public static final MemoryLayout CLASSIC_1K = new MemoryLayout(16);
	public static final MemoryLayout CLASSIC_4K = new MemoryLayout(FOUR_BLOCK_SECTORS + SIXTEEN_BLOCK_SECTORS);

	private int sectors;

	public MemoryLayout(int sectors) {
		this.sectors = sectors;
	}

	public int getMadVersion() {
		if (sectors == 16)
			return MadConstants.GPB_MAD_V1;
		else
			return MadConstants.GPB_MAD_V2;
	}

	public int getSectors() {
		return sectors;
	}

	public int getBlocksPerSector(int sectorId) {
		if (sectorId < FOUR_BLOCK_SECTORS)
			return 4;
		else
			return 16;
	}

	public int getDataBlocksPerSector(int sectorId) {
		if (sectorId < FOUR_BLOCK_SECTORS)
			return 3;
		else
			return 15;
	}

	public int getBlockNumber(int sectorId, int blockId) {
		if (sectorId < FOUR_BLOCK_SECTORS)
			return sectorId * 4 + blockId;
		else
			return (FOUR_BLOCK_SECTORS * 4) + ((sectorId - FOUR_BLOCK_SECTORS) * 16 + blockId);
	}

	public int getTrailerBlockNumberForSector(int sectorId) {
		return getBlocksPerSector(sectorId) - 1;
	}

	public boolean isTrailerBlock(int sectorId, int blockId) {
		return blockId == getBlocksPerSector(sectorId) - 1;
	}

}

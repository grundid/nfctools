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
package org.nfctools.mf.card;

import org.nfctools.api.TagType;

@Deprecated
public class MfCard4k extends MfCard {

	private final int FOUR_BLOCK_SECTORS = 32;
	private final int SIXTEEN_BLOCK_SECTORS = 8;

	public MfCard4k(byte[] cardId, Object connectionToken) {
		super(TagType.MIFARE_CLASSIC_4K, cardId, connectionToken);
	}

	@Override
	public int getSectors() {
		return FOUR_BLOCK_SECTORS + SIXTEEN_BLOCK_SECTORS;
	}

	@Override
	public int getBlocksPerSector(int sectorId) {
		if (sectorId < FOUR_BLOCK_SECTORS)
			return 4;
		else
			return 16;
	}

	@Override
	public int getDataBlocksPerSector(int sectorId) {
		if (sectorId < FOUR_BLOCK_SECTORS)
			return 3;
		else
			return 15;
	}

	@Override
	public int getBlockNumber(int sectorId, int blockId) {
		if (sectorId < FOUR_BLOCK_SECTORS)
			return sectorId * 4 + blockId;
		else
			return (FOUR_BLOCK_SECTORS * 4) + ((sectorId - FOUR_BLOCK_SECTORS) * 16 + blockId);
	}
}

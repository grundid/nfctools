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
public class MfCard1k extends MfCard {

	public MfCard1k(byte[] cardId, Object connectionToken) {
		super(TagType.MIFARE_CLASSIC_1K, cardId, connectionToken);
	}

	@Override
	public int getSectors() {
		return 16;
	}

	@Override
	public int getBlocksPerSector(int sectorId) {
		return 4;
	}

	@Override
	public int getDataBlocksPerSector(int sectorId) {
		return 3;
	}

	@Override
	public int getBlockNumber(int sectorId, int blockId) {
		int blockNumber = sectorId * 4 + blockId;
		return blockNumber;
	}

}

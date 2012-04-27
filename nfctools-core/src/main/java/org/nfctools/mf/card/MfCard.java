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

import org.nfctools.api.Tag;
import org.nfctools.api.TagType;
import org.nfctools.utils.NfcUtils;

@Deprecated
public abstract class MfCard extends Tag {

	private Object connectionToken;

	protected MfCard(TagType tagType, byte[] generalBytes, Object connectionToken) {
		super(tagType, generalBytes);
		this.connectionToken = connectionToken;
	}

	public abstract int getSectors();

	public abstract int getBlocksPerSector(int sectorId);

	public abstract int getDataBlocksPerSector(int sectorId);

	public abstract int getBlockNumber(int sectorId, int blockId);

	public int getTrailerBlockNumberForSector(int sectorId) {
		return getBlocksPerSector(sectorId) - 1;
	}

	public boolean isTrailerBlock(int sectorId, int blockId) {
		return blockId == getBlocksPerSector(sectorId) - 1;
	}

	public Object getConnectionToken() {
		return connectionToken;
	}

	private String getCardIdAsHex() {
		return NfcUtils.convertBinToASCII(getGeneralBytes());
	}

	@Override
	public String toString() {
		return "ID: " + getCardIdAsHex() + " ConnectionToken: " + connectionToken;
	}
}

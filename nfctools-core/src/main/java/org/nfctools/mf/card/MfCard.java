/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
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
import org.nfctools.utils.NfcUtils;

public abstract class MfCard implements Tag {

	private Object connectionToken;
	private byte[] nfcId;

	protected MfCard(byte[] nfcId, Object connectionToken) {
		this.nfcId = nfcId;
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

	@Override
	public int getMode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public byte[] getNfcId() {
		return nfcId;
	}

	@Override
	public byte[] getGeneralBytes() {
		return new byte[0];
	}

	public Object getConnectionToken() {
		return connectionToken;
	}

	public String getCardIdAsHex() {
		return NfcUtils.convertBinToASCII(nfcId);
	}

	@Override
	public String toString() {
		return "ID: " + getCardIdAsHex() + " ConnectionToken: " + connectionToken;
	}
}

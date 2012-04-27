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
package org.nfctools.mf;

import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.classic.Key;

@Deprecated
public class MfAccess extends SimpleMfAccess {

	private int sector;
	private int block;
	private int blocksToRead = 1;

	public MfAccess(SimpleMfAccess simpleMfAccess, int sector, int block, int blocksToRead) {
		super(simpleMfAccess.getCard(), simpleMfAccess.getKey(), simpleMfAccess.getKeyValue());
		this.sector = sector;
		this.block = block;
		this.blocksToRead = blocksToRead;
	}

	public MfAccess(MfCard card, int sector, int block, Key key, byte[] keyValue) {
		super(card, key, keyValue);
		this.sector = sector;
		this.block = block;
	}

	public MfAccess(MfCard card, int sector, int block, int blocksToRead, Key key, byte[] keyValue) {
		super(card, key, keyValue);
		this.sector = sector;
		this.block = block;
		this.blocksToRead = blocksToRead;
	}

	public int getSector() {
		return sector;
	}

	public void setSector(int sector) {
		this.sector = sector;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public int getBlocksToRead() {
		return blocksToRead;
	}
}

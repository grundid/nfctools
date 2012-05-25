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


public class MfClassicAccess {

	private KeyValue keyValue;
	private int sector;
	private int block;
	private int blocksToRead = 1;

	public MfClassicAccess(KeyValue keyValue, int sector, int block, int blocksToRead) {
		this.keyValue = keyValue;
		this.sector = sector;
		this.block = block;
		this.blocksToRead = blocksToRead;
	}

	public MfClassicAccess(KeyValue keyValue, int sector, int block) {
		this.keyValue = keyValue;
		this.sector = sector;
		this.block = block;
	}

	public KeyValue getKeyValue() {
		return keyValue;
	}

	public int getSector() {
		return sector;
	}

	public int getBlock() {
		return block;
	}

	public int getBlocksToRead() {
		return blocksToRead;
	}

}

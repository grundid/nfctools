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
package org.nfctools.test;

import java.io.IOException;

import org.nfctools.api.TagInfo;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.ul.DataBlock;
import org.nfctools.mf.ul.MemoryMap;
import org.nfctools.mf.ul.MfUlReaderWriter;

public class MemoryMapUlReaderWriter implements MfUlReaderWriter {

	private MemoryMap memoryMap;

	public MemoryMapUlReaderWriter(MemoryMap memoryMap) {
		this.memoryMap = memoryMap;
	}

	public MemoryMap getMemoryMap() {
		return memoryMap;
	}

	@Override
	public MfBlock[] readBlock(int startPage, int pagesToRead) throws IOException {
		MfBlock[] blocks = new MfBlock[pagesToRead];
		for (int x = 0; x < pagesToRead; x++) {
			blocks[x] = new DataBlock(memoryMap.getPage(startPage + x));
		}
		return blocks;
	}

	@Override
	public void writeBlock(int startPage, MfBlock... mfBlock) throws IOException {
		for (int x = 0; x < mfBlock.length; x++) {
			memoryMap.setPage(startPage + x, mfBlock[x].getData());
		}
	}

	@Override
	public TagInfo getTagInfo() throws IOException {
		throw new RuntimeException("not implemented yet");
	}
}

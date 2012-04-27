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
package org.nfctools.mf.block;

import org.nfctools.mf.MfException;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.classic.MemoryLayout;

public class BlockResolver {

	@Deprecated
	public static MfBlock resolveBlock(MfCard card, int sectorId, int blockId, byte[] data) throws MfException {
		if (card.isTrailerBlock(sectorId, blockId))
			return new TrailerBlock(data);

		if ((sectorId == 0) && (blockId == 0))
			return new ManufacturerBlock(data);

		if (ValueBlock.isValidValueBlock(data)) {
			return new ValueBlock(data);
		}
		return new DataBlock(data);
	}

	// TODO move it to readerwriter
	public static MfBlock resolveBlock(MemoryLayout memoryLayout, int sectorId, int blockId, byte[] data)
			throws MfException {
		if (memoryLayout.isTrailerBlock(sectorId, blockId))
			return new TrailerBlock(data);

		if ((sectorId == 0) && (blockId == 0))
			return new ManufacturerBlock(data);

		if (ValueBlock.isValidValueBlock(data)) {
			return new ValueBlock(data);
		}
		return new DataBlock(data);
	}
}

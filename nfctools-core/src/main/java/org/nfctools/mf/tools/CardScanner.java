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
package org.nfctools.mf.tools;

import java.io.IOException;

import org.nfctools.mf.Key;
import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.utils.NfcUtils;

public class CardScanner extends AbstractCardTool {

	@Override
	public void doWithCard(MfCard card, MfReaderWriter readerWriter) throws IOException {
		for (int sectorId = 0; sectorId < card.getSectors(); sectorId++) {
			for (byte[] key : knownKeys) {
				try {
					MfBlock[] mfBlock = readerWriter.readBlock(new MfAccess(card, sectorId, 0, card
							.getBlocksPerSector(sectorId), Key.A, key));
					for (int blockId = 0; blockId < mfBlock.length; blockId++) {
						System.out.println("S" + sectorId + "|B" + blockId + " Key: " + NfcUtils.convertBinToASCII(key)
								+ " " + mfBlock[blockId]);
					}
					break;
				}
				catch (MfLoginException e) {
					log.info("Cannot read sector: " + sectorId + " with key " + NfcUtils.convertBinToASCII(key));
				}
			}
		}
	}
}

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

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.block.DataBlock;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.classic.KeyValue;
import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.classic.MfClassicAccess;
import org.nfctools.mf.classic.MfClassicReaderWriter;
import org.nfctools.utils.NfcUtils;

public class MfClassicCardCleaner extends AbstractCardTool {

	private TrailerBlock readTrailerBlock(MfClassicReaderWriter readerWriter, int sectorId) throws IOException {
		MemoryLayout memoryLayout = readerWriter.getMemoryLayout();
		for (byte[] key : knownKeys) {
			try {
				MfClassicAccess access = new MfClassicAccess(new KeyValue(Key.A, key), sectorId,
						memoryLayout.getTrailerBlockNumberForSector(sectorId));
				MfBlock[] readBlock = readerWriter.readBlock(access);
				return (TrailerBlock)readBlock[0];
			}
			catch (MfLoginException e) {
			}
		}
		return null;
	}

	@Override
	public void doWithReaderWriter(MfClassicReaderWriter readerWriter) throws IOException {
		DataBlock emptyDataBlock = new DataBlock();
		MemoryLayout memoryLayout = readerWriter.getMemoryLayout();

		for (int sectorId = 0; sectorId < memoryLayout.getSectors(); sectorId++) {
			TrailerBlock trailerBlock = readTrailerBlock(readerWriter, sectorId);
			if (trailerBlock != null) {
				Key keyToWrite = trailerBlock.isKeyBReadable() ? Key.A : Key.B;
				for (byte[] key : knownKeys) {
					try {
						initTransportConfig(readerWriter, sectorId, new KeyValue(keyToWrite, key));
						for (int blockId = 0; blockId < memoryLayout.getBlocksPerSector(sectorId); blockId++) {
							if (!memoryLayout.isTrailerBlock(sectorId, blockId) && !(blockId == 0 && sectorId == 0)) {
								log.info("Cleaning S" + sectorId + "|B" + blockId);
								MfClassicAccess access = new MfClassicAccess(new KeyValue(Key.A,
										MfConstants.TRANSPORT_KEY), sectorId, blockId);
								readerWriter.writeBlock(access, emptyDataBlock);
							}
						}
						log.info("Sector " + sectorId + " clear with key: " + NfcUtils.convertBinToASCII(key));
						break;
					}
					catch (MfLoginException e) {
						log.info("Cannot clear sector: " + sectorId + " with key " + NfcUtils.convertBinToASCII(key));
					}
				}
			}
			else {
				log.info("Cannot read trailer block in sector: " + sectorId);
			}
		}
		log.info("Done!");
	}

	public static void initTransportConfig(MfClassicReaderWriter readerWriter, int sector, KeyValue keyValue)
			throws IOException {
		MemoryLayout memoryLayout = readerWriter.getMemoryLayout();
		TrailerBlock transportTrailer = new TrailerBlock();
		MfClassicAccess access = new MfClassicAccess(keyValue, sector,
				memoryLayout.getTrailerBlockNumberForSector(sector));
		readerWriter.writeBlock(access, transportTrailer);
	}

}

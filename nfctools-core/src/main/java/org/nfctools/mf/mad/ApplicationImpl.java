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
package org.nfctools.mf.mad;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.nfctools.NfcException;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.block.DataBlock;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.classic.KeyValue;
import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.classic.MfClassicAccess;
import org.nfctools.mf.classic.MfClassicReaderWriter;

public class ApplicationImpl implements Application {

	private ApplicationId applicationId;
	private MemoryLayout memoryLayout;
	private MfClassicReaderWriter readerWriter;
	private int firstSlot;
	private int lastSlot;
	private int allocatedSize;

	private AbstractMad mad;
	private TrailerBlock trailerBlock;

	public ApplicationImpl(ApplicationId applicationId, int allocatedSize, MfClassicReaderWriter readerWriter,
			int firstSlot, int lastSlot, AbstractMad mad) {
		this.applicationId = applicationId;
		this.allocatedSize = allocatedSize;
		this.readerWriter = readerWriter;
		this.firstSlot = firstSlot;
		this.lastSlot = lastSlot;
		this.mad = mad;
		this.memoryLayout = readerWriter.getMemoryLayout();
	}

	@Override
	public byte[] getApplicationId() {
		return applicationId.getAid();
	}

	@Override
	public int getAllocatedSize() {
		return allocatedSize;
	}

	@Override
	public byte[] read(KeyValue keyValue) throws IOException {
		// TODO create SlotIterator
		ByteArrayOutputStream baos = new ByteArrayOutputStream(allocatedSize);
		for (int slot = firstSlot; slot <= lastSlot; slot++) {
			int sectorId = mad.getSectorIdForSlot(slot);
			try {
				readBlockData(keyValue, baos, sectorId);
			}
			catch (MfLoginException e) {
				readBlockData(new KeyValue(keyValue.getKey(), MfConstants.TRANSPORT_KEY), baos, sectorId);
			}
		}
		return baos.toByteArray();
	}

	private void readBlockData(KeyValue keyValue, ByteArrayOutputStream baos, int sectorId) throws IOException {
		MfClassicAccess access = new MfClassicAccess(keyValue, sectorId, 0,
				memoryLayout.getDataBlocksPerSector(sectorId));
		MfBlock[] blocks = readerWriter.readBlock(access);
		for (MfBlock block : blocks) {
			baos.write(block.getData());
		}
	}

	@Override
	public void write(KeyValue keyValue, byte[] content) throws IOException {

		if (content.length <= getAllocatedSize()) {

			ByteArrayInputStream bais = new ByteArrayInputStream(content);

			for (int slot = firstSlot; slot <= lastSlot; slot++) {
				int sectorId = mad.getSectorIdForSlot(slot);
				for (int blockId = 0; blockId < memoryLayout.getDataBlocksPerSector(sectorId); blockId++) {

					MfClassicAccess access = new MfClassicAccess(keyValue, sectorId, blockId);

					byte[] buffer = new byte[MfConstants.BYTES_PER_BLOCK];
					if (bais.available() > 0)
						bais.read(buffer);

					DataBlock dataBlock = new DataBlock(buffer);
					readerWriter.writeBlock(access, dataBlock);
				}
			}
		}
		else {
			throw new IllegalArgumentException("content length too big for allocated space");
		}
	}

	@Override
	public void updateTrailer(KeyValue keyValue, TrailerBlock trailerBlock) throws IOException {
		for (int slot = firstSlot; slot <= lastSlot; slot++) {
			int sectorId = mad.getSectorIdForSlot(slot);
			MfClassicAccess access = new MfClassicAccess(keyValue, sectorId,
					memoryLayout.getTrailerBlockNumberForSector(sectorId));

			readerWriter.writeBlock(access, trailerBlock);
		}
		this.trailerBlock = trailerBlock;
	}

	@Override
	public TrailerBlock readTrailer(KeyValue keyValue) throws IOException {
		if (trailerBlock == null) {
			for (int slot = firstSlot; slot <= lastSlot; slot++) {
				int sectorId = mad.getSectorIdForSlot(slot);
				MfClassicAccess access = new MfClassicAccess(keyValue, sectorId,
						memoryLayout.getTrailerBlockNumberForSector(sectorId));

				TrailerBlock block = (TrailerBlock)readerWriter.readBlock(access)[0];
				if (trailerBlock == null) {
					trailerBlock = block;
				}
				else {
					if (!Arrays.equals(block.getAccessConditions(), trailerBlock.getAccessConditions())
							|| (block.getGeneralPurposeByte() != trailerBlock.getGeneralPurposeByte()))
						throw new NfcException(
								"Not all trailer blocks are equal for the APP-ID. Somebody tempered with the tag.");
				}

			}
		}
		return trailerBlock;
	}

	@Override
	public void makeReadOnly(KeyValue keyValue) throws IOException {
		readTrailer(keyValue);
		trailerBlock.setAccessConditions(MfConstants.NDEF_READ_ONLY_ACCESS_CONDITIONS);
		trailerBlock.setGeneralPurposeByte(MfConstants.NDEF_GPB_V10_READ_ONLY);
		updateTrailer(keyValue, trailerBlock);
	}

	@Override
	public ApplicationDirectory getApplicationDirectory() {
		return mad;
	}

}

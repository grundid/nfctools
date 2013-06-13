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
package org.nfctools.mf.ul;

import java.io.IOException;

import org.nfctools.NfcException;
import org.nfctools.api.ApduTag;
import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagInfo;
import org.nfctools.api.TagType;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.ndef.NdefOperationsListener;
import org.nfctools.spi.acs.AcrMfUlReaderWriter;

public class Type2NfcTagListener implements NfcTagListener {

	private NdefOperationsListener ndefListener;

	public Type2NfcTagListener() {
	}

	public Type2NfcTagListener(NdefOperationsListener ndefListener) {
		this.ndefListener = ndefListener;
	}

	public void setNdefListener(NdefOperationsListener ndefListener) {
		this.ndefListener = ndefListener;
	}

	@Override
	public boolean canHandle(Tag tag) {
		return tag.getTagType().equals(TagType.MIFARE_ULTRALIGHT);
	}

	@Override
	public void handleTag(Tag tag) {
		Type2NdefOperations ndefOperations = createNdefOperations((ApduTag)tag);
		if (ndefListener != null)
			ndefListener.onNdefOperations(ndefOperations);
	}

	protected Type2NdefOperations createNdefOperations(ApduTag tag) {
		MfUlReaderWriter readerWriter = new AcrMfUlReaderWriter(tag);
		MemoryLayout memoryLayout = null;
		boolean formatted = false;
		boolean writable = false;
		TagInfo tagInfo = null;
		try {
			tagInfo = readerWriter.getTagInfo();
			MfBlock[] initBlocks = readerWriter.readBlock(0, 5);
			CapabilityBlock capabilityBlock = new CapabilityBlock(initBlocks[3].getData());
			if (UltralightHandler.isBlank(initBlocks)) {
				if (UltralightHandler.isUltralight(initBlocks[4].getData())) {
					memoryLayout = MemoryLayout.ULTRALIGHT;
				}
				else if (UltralightHandler.isUltralightC(initBlocks[4].getData())) {
					memoryLayout = MemoryLayout.ULTRALIGHT_C;
				}
				else {
					throw new NfcException("Unknown tag size");
				}
				writable = true;
			}
			else if (UltralightHandler.isFormatted(initBlocks)) {
				formatted = true;
				if (capabilityBlock.getSize() == 0x06)
					memoryLayout = MemoryLayout.ULTRALIGHT;
				else if (capabilityBlock.getSize() == 0x12) {
					memoryLayout = MemoryLayout.ULTRALIGHT_C;
				}
				else {
					throw new NfcException("Unknown memory size " + capabilityBlock.getSize());
				}
				writable = !capabilityBlock.isReadOnly() && !isLocked(readerWriter, memoryLayout);
			}
			else {
				throw new NfcException("Unknown tag contents");
			}
		}
		catch (Exception e) {
			throw new NfcException(e);
		}
		return new Type2NdefOperations(memoryLayout, readerWriter, tagInfo, formatted, writable);
	}

	private boolean isLocked(MfUlReaderWriter readerWriter, MemoryLayout memoryLayout) throws IOException {
		for (LockPage lockPage : memoryLayout.getLockPages()) {
			MfBlock[] block = readerWriter.readBlock(lockPage.getPage(), 1);
			for (int lockByte : lockPage.getLockBytes()) {
				if (block[0].getData()[lockByte] != 0)
					return true;
			}
		}
		return false;
	}
}

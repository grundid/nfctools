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

import java.io.IOException;

import org.nfctools.NfcException;
import org.nfctools.api.ApduTag;
import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagInfo;
import org.nfctools.api.TagType;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.mad.Application;
import org.nfctools.mf.mad.ApplicationDirectory;
import org.nfctools.ndef.NdefOperationsListener;
import org.nfctools.spi.acs.AcrMfClassicReaderWriter;

public class MfClassicNfcTagListener implements NfcTagListener {

	private NdefOperationsListener ndefListener;

	public MfClassicNfcTagListener() {
	}

	public MfClassicNfcTagListener(NdefOperationsListener ndefListener) {
		this.ndefListener = ndefListener;
	}

	@Override
	public boolean canHandle(Tag tag) {
		return tag.getTagType().equals(TagType.MIFARE_CLASSIC_1K) || tag.getTagType().equals(TagType.MIFARE_CLASSIC_4K);
	}

	@Override
	public void handleTag(Tag tag) {
		MemoryLayout memoryLayout = tag.getTagType().equals(TagType.MIFARE_CLASSIC_1K) ? MemoryLayout.CLASSIC_1K
				: MemoryLayout.CLASSIC_4K;
		MfClassicNdefOperations ndefOperations = createNdefOperations((ApduTag)tag, memoryLayout);
		if (ndefListener != null)
			ndefListener.onNdefOperations(ndefOperations);
	}

	protected MfClassicNdefOperations createNdefOperations(ApduTag tag, MemoryLayout memoryLayout) {
		boolean formatted = false;
		boolean writable = false;
		MfClassicReaderWriter readerWriter = new AcrMfClassicReaderWriter(tag, memoryLayout);
		TagInfo tagInfo = null;
		try {
			tagInfo = readerWriter.getTagInfo();
			if (readerWriter.hasApplicationDirectory()) {
				ApplicationDirectory applicationDirectory = readerWriter.getApplicationDirectory();
				if (applicationDirectory.hasApplication(MfConstants.NDEF_APP_ID)) {
					formatted = true;
					Application application = applicationDirectory.openApplication(MfConstants.NDEF_APP_ID);
					writable = ClassicHandler.isFormattedWritable(application, MfClassicConstants.NDEF_KEY);
				}
				else {
					throw new NfcException("Unknown tag contents");
				}
			}
			else {
				if (ClassicHandler.isBlank(readerWriter)) {
					writable = true;
				}
				else
					throw new NfcException("Unknown tag contents");
			}
		}
		catch (IOException e) {
			throw new NfcException(e);
		}
		return new MfClassicNdefOperations(readerWriter, tagInfo, formatted, writable);
	}
}

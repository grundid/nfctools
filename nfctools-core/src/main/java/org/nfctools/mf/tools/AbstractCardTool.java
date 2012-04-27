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
import java.util.ArrayList;
import java.util.Collection;

import org.nfctools.api.ApduTag;
import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagType;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.classic.MfClassicReaderWriter;
import org.nfctools.mf.mad.MadConstants;
import org.nfctools.spi.acs.AcrMfClassicReaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCardTool implements NfcTagListener {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected Collection<byte[]> knownKeys = new ArrayList<byte[]>();

	public AbstractCardTool() {
		knownKeys.add(MfConstants.NDEF_KEY);
		knownKeys.add(MfConstants.TRANSPORT_KEY);
		knownKeys.add(MadConstants.DEFAULT_MAD_KEY);
		//		knownKeys.add(NfcUtils.convertASCIIToBin("AABBCCDDEEFF"));
	}

	public void addKnownKey(byte[] key) {
		knownKeys.add(key);
	}

	public abstract void doWithReaderWriter(MfClassicReaderWriter readerWriter) throws IOException;

	@Override
	public boolean canHandle(Tag tag) {
		return tag.getTagType().equals(TagType.MIFARE_CLASSIC_1K) || tag.getTagType().equals(TagType.MIFARE_CLASSIC_4K);
	}

	@Override
	public void handleTag(Tag tag) {
		MemoryLayout memoryLayout = tag.getTagType().equals(TagType.MIFARE_CLASSIC_1K) ? MemoryLayout.CLASSIC_1K
				: MemoryLayout.CLASSIC_4K;
		MfClassicReaderWriter readerWriter = new AcrMfClassicReaderWriter((ApduTag)tag, memoryLayout);
		try {
			doWithReaderWriter(readerWriter);
		}
		catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

}

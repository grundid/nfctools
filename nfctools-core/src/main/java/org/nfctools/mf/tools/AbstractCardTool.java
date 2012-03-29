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

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.mad.MadConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCardTool {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected Collection<byte[]> knownKeys = new ArrayList<byte[]>();

	public AbstractCardTool() {
		knownKeys.add(MfConstants.TRANSPORT_KEY);
		knownKeys.add(MfConstants.NDEF_KEY);
		knownKeys.add(MadConstants.DEFAULT_MAD_KEY);
		//		knownKeys.add(NfcUtils.convertASCIIToBin("AABBCCDDEEFF"));
	}

	public void addKnownKey(byte[] key) {
		knownKeys.add(key);
	}

	public abstract void doWithCard(MfCard card, MfReaderWriter readerWriter) throws IOException;

}

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
package org.nfctools.nfcip;

import java.io.IOException;

import org.nfctools.api.ApduTag;
import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagType;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.spi.acs.ApduTagReaderWriter;
import org.nfctools.spi.tama.nfcip.TamaNfcIpCommunicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NFCIPTagListener implements NfcTagListener {

	private Logger log = LoggerFactory.getLogger(getClass());
	private NFCIPConnectionListener nfcipConnectionListener;

	public NFCIPTagListener(NFCIPConnectionListener nfcipConnectionListener) {
		this.nfcipConnectionListener = nfcipConnectionListener;
	}

	@Override
	public boolean canHandle(Tag tag) {
		return tag.getTagType().equals(TagType.NFCIP);
	}

	@Override
	public void handleTag(Tag tag) {
		ApduTagReaderWriter apduReaderWriter = new ApduTagReaderWriter((ApduTag)tag);

		TamaNfcIpCommunicator nfcIpCommunicator = new TamaNfcIpCommunicator(apduReaderWriter, apduReaderWriter);
		nfcIpCommunicator.setConnectionSetup(LlcpConstants.CONNECTION_SETUP);
		try {
			NFCIPConnection nfcipConnection = nfcIpCommunicator.connectAsInitiator();
			log.info("Connection: " + nfcipConnection);
			handleNfcipConnection(nfcipConnection);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void handleNfcipConnection(NFCIPConnection nfcipConnection) throws IOException {
		if (nfcipConnection != null && nfcipConnectionListener != null) {
			nfcipConnectionListener.onConnection(nfcipConnection);
		}
	}
}

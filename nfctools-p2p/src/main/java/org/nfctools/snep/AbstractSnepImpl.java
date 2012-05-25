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
package org.nfctools.snep;

import org.nfctools.llcp.Llcp;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.llcp.LlcpSocket;
import org.nfctools.llcp.ServiceAccessPoint;

public abstract class AbstractSnepImpl implements ServiceAccessPoint {

	protected byte snepVersion = 0x10;
	protected int maxInformationUnit = LlcpConstants.DEFAULT_MIU;

	protected FragmentReader reader = new FragmentReader();
	protected FragmentIterator fragmentIterator;
	protected SnepMessage continueMessage;

	protected AbstractSnepImpl(byte continueMessageCode) {
		continueMessage = new SnepMessage(snepVersion, continueMessageCode);
	}

	@Override
	public void onLlcpActive(Llcp llcp) {
	}

	@Override
	public void onConnectFailed() {
	}

	@Override
	public void onConnectSucceeded(LlcpSocket llcpSocket) {
	}

	@Override
	public void onSendFailed() {
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public void onConnectionActive(LlcpSocket llcpSocket) {
	}

	@Override
	public byte[] onInformation(byte[] serviceDataUnit) {

		reader.addFragment(serviceDataUnit);

		if (reader.isComplete()) {
			byte[] completeMessage = reader.getCompleteMessage();
			reader.reset();
			SnepMessage snepMessage = new SnepMessage(completeMessage);
			if (snepMessage.getVersion() == snepVersion)
				return processMessage(snepMessage);
			else
				return new SnepMessage(snepVersion, Response.UNSUPPORTED_VERSION).getBytes();
		}
		else {
			if (reader.isFirstFragment()) {
				return continueMessage.getBytes();
			}
			else
				return null;
		}
	}

	protected abstract byte[] processMessage(SnepMessage snepMessage);

}

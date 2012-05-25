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
package org.nfctools.ndefpush;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.nfctools.llcp.Llcp;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.llcp.LlcpSocket;
import org.nfctools.llcp.ServiceAccessPoint;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefListener;
import org.nfctools.ndef.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NdefPushLlcpService implements ServiceAccessPoint {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ByteArrayOutputStream incommingBuffer = new ByteArrayOutputStream();
	private NdefListener ndefListener;

	private OutgoingNdefMessage outgoingMessage;
	private BlockingQueue<WaitingNdefMessage> waitingMessages = new LinkedBlockingDeque<WaitingNdefMessage>();

	public NdefPushLlcpService(NdefListener ndefListener) {
		this.ndefListener = ndefListener;
	}

	public void addMessages(Collection<Record> ndefRecords, NdefPushFinishListener finishListener) {
		waitingMessages.add(new WaitingNdefMessage(ndefRecords, finishListener));
	}

	public boolean hasMessagesToSend() {
		return !waitingMessages.isEmpty();
	}

	@Override
	public void onLlcpActive(Llcp llcp) {
		log.debug(waitingMessages.size() + " NDEF messages to send");
		if (hasMessagesToSend())
			llcp.connectToService(LlcpConstants.COM_ANDROID_NPP, this);
	}

	@Override
	public void onConnectionActive(LlcpSocket llcpSocket) {
		if (hasMessagesToSend())
			handleActiveConnection(llcpSocket);
		else
			llcpSocket.disconnect();
	}

	@Override
	public void onConnectFailed() {
		log.debug("Connection failed");

	}

	@Override
	public void onConnectSucceeded(LlcpSocket llcpSocket) {
		handleActiveConnection(llcpSocket);
	}

	private void handleActiveConnection(LlcpSocket llcpSocket) {
		outgoingMessage = new OutgoingNdefMessage();
		while (waitingMessages.size() > 0) {
			outgoingMessage.addWaitingNdefMessage(waitingMessages.poll());
		}
		outgoingMessage.compile();

		log.debug("Connection ok, sending message with " + outgoingMessage.getAvailableBytes() + " bytes");
		sendMessage(llcpSocket);
	}

	@Override
	public void onSendSucceeded(LlcpSocket llcpSocket) {
		if (waitingMessages != null) {
			sendMessage(llcpSocket);
		}
	}

	private void sendMessage(LlcpSocket llcpSocket) {
		int messageSize = Math.min(outgoingMessage.getAvailableBytes(), llcpSocket.getMaximumInformationUnit());
		if (messageSize > 0) {
			byte[] messageBuffer = new byte[messageSize];
			outgoingMessage.readNextBuffer(messageBuffer);
			llcpSocket.sendMessage(messageBuffer);
		}
		else {
			if (waitingMessages.isEmpty()) {
				llcpSocket.disconnect();
			}
			else {
				onConnectSucceeded(llcpSocket);
			}
			log.debug("Message send");
			outgoingMessage.notifyFinishListenerSuccess();
			outgoingMessage = null;
		}
	}

	@Override
	public void onSendFailed() {
		log.debug("Send failed");
		if (outgoingMessage != null) {
			outgoingMessage.notifyFinishListenerFailure();
			outgoingMessage = null;
		}
	}

	@Override
	public boolean canAcceptConnection(Object[] parameters) {
		boolean canAcceptConnection = ndefListener != null;
		log.debug("can connect: " + canAcceptConnection);
		return canAcceptConnection;
	}

	@Override
	public void onDisconnect() {
		log.debug("Remote disconnect");
		outgoingMessage = null;
	}

	@Override
	public byte[] onInformation(byte[] serviceDataUnit) {
		try {
			incommingBuffer.write(serviceDataUnit);
			try {
				List<byte[]> ndefMessages = NdefPushProtocol.parse(incommingBuffer.toByteArray());
				if (ndefMessages != null) {
					for (byte[] ndef : ndefMessages) {
						List<Record> records = NdefContext.getNdefMessageDecoder().decodeToRecords(ndef);
						if (ndefListener != null) {
							ndefListener.onNdefMessages(records);
						}
					}
				}
				incommingBuffer.reset();
			}
			catch (FormatException e) {
				// Message incomplete, wait for more data
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}

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
package org.nfctools.llcp.pdu;

import org.nfctools.llcp.Llcp;
import org.nfctools.llcp.LlcpSocket;
import org.nfctools.llcp.ServiceAccessPoint;

public class DummyServiceAccessPoint implements ServiceAccessPoint {

	private String messageToSend;
	private String messageReceived;
	private String serviceToConnect;

	private boolean acceptConnections = true;
	private boolean connected = false;

	public DummyServiceAccessPoint() {
	}

	public DummyServiceAccessPoint(String serviceToConnect) {
		this.serviceToConnect = serviceToConnect;
	}

	public DummyServiceAccessPoint(String message, String serviceToConnect) {
		this.messageToSend = message;
		this.serviceToConnect = serviceToConnect;
	}

	@Override
	public void onConnectFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLlcpActive(Llcp llcp) {
		if (messageToSend != null) {
			llcp.connectToService(serviceToConnect, this);
		}
	}

	@Override
	public void onConnectionActive(LlcpSocket llcpSocket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectSucceeded(LlcpSocket llcpSocket) {
		connected = true;
		llcpSocket.sendMessage(messageToSend.getBytes());
	}

	@Override
	public void onSendSucceeded(LlcpSocket llcpSocket) {
		if (messageToSend != null) {
			messageToSend = null;
			llcpSocket.disconnect();
		}

	}

	@Override
	public void onSendFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canAcceptConnection(Object[] parameters) {
		return acceptConnections;
	}

	@Override
	public byte[] onInformation(byte[] serviceDataUnit) {
		messageReceived = new String(serviceDataUnit);
		return null;
	}

	public String getMessageReceived() {
		return messageReceived;
	}

	@Override
	public void onDisconnect() {
		connected = false;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setAcceptConnections(boolean acceptConnections) {
		this.acceptConnections = acceptConnections;
	}
}

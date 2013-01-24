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
package org.nfctools.llcp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nfctools.llcp.parameter.LinkTimeOut;
import org.nfctools.llcp.parameter.Miux;
import org.nfctools.llcp.parameter.ServiceName;
import org.nfctools.llcp.parameter.Version;
import org.nfctools.llcp.pdu.AbstractProtocolDataUnit;
import org.nfctools.llcp.pdu.Connect;
import org.nfctools.llcp.pdu.ConnectComplete;
import org.nfctools.llcp.pdu.Disconnect;
import org.nfctools.llcp.pdu.DisconnectedMode;
import org.nfctools.llcp.pdu.Symmetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LlcpConnectionManager implements Llcp {

	private Logger log = LoggerFactory.getLogger(getClass());
	private final int SERVICE_DISCOVERY_ADDRESS = 1;
	private final int PREFERRED_MIUX = 120;
	private final int MAX_CONNECT_WAIT = 200;
	private static final int MAX_RETRIES = 4;
	private static final byte VERSION_MAJOR = 1;
	private int miuExtension = PREFERRED_MIUX;
	private int linkTimeOut = MAX_CONNECT_WAIT;
	private ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
	private Map<Integer, PendingConnection> pendingConnections = new HashMap<Integer, PendingConnection>();
	private Map<Integer, LlcpSocket> openConnections = new HashMap<Integer, LlcpSocket>();
	private Map<Integer, ServiceAccessPoint> services = new HashMap<Integer, ServiceAccessPoint>();
	private AbstractProtocolDataUnit messageToSend = null;

	public void registerWellKnownServiceAccessPoint(String serviceName, ServiceAccessPoint serviceAccessPoint) {
		serviceDiscovery.registerSerivce(serviceName, serviceAccessPoint);
	}

	public void registerServiceAccessPoint(ServiceAccessPoint serviceAccessPoint) {
		services.put(getFreeLocalServiceAddress(), serviceAccessPoint);
	}

	public void registerServiceAccessPoint(int serviceAddress, ServiceAccessPoint serviceAccessPoint) {
		services.put(serviceAddress, serviceAccessPoint);
	}

	private Integer getFreeLocalServiceAddress() {
		for (int x = 16; x < 32; x++) {
			Integer address = Integer.valueOf(x);
			if (!services.containsKey(address))
				return address;
		}
		throw new LlcpException("No more free ports");
	}

	private Integer getFreeOutgoingAddress() {
		Set<Integer> usedAddresses = new HashSet<Integer>();
		usedAddresses.addAll(openConnections.keySet());
		usedAddresses.addAll(pendingConnections.keySet());
		for (int x = 32; x < 64; x++) {
			Integer address = Integer.valueOf(x);
			if (!usedAddresses.contains(address))
				return address;
		}
		throw new LlcpException("No more free ports");
	}

	public void clearConnections() {
		for (PendingConnection pendingConnection : pendingConnections.values()) {
			try {
				pendingConnection.getServiceAccessPoint().onDisconnect();
			}
			catch (Exception e) {
				log.warn("Error closing pending connection", e);
			}
		}
		for (LlcpSocket llcpSocket : openConnections.values()) {
			try {
				llcpSocket.disconnect();
			}
			catch (Exception e) {
				log.warn("Error closing open connection", e);
			}
		}
		pendingConnections.clear();
		openConnections.clear();
		messageToSend = null;
	}

	public ServiceAccessPoint getServiceAccessPoint(int address, String serviceName) {
		if (address == 1)
			return serviceDiscovery.getService(serviceName);
		else {
			return services.get(Integer.valueOf(address));
		}
	}

	public Collection<Object> getParameter() {
		return Collections.emptyList();
	}

	public AbstractProtocolDataUnit onLlcpActive() {
		// TODO make messageToSend method local variable, implement Llcp interface similar to SnepAgent
		messageToSend = new Symmetry();
		handlePendingConnectionTimeout();
		if (pendingConnections.isEmpty()) {
			serviceDiscovery.onLlcpActive(this);
			for (Entry<Integer, ServiceAccessPoint> entry : services.entrySet()) {
				ServiceAccessPoint serviceAccessPoint = entry.getValue();
				serviceAccessPoint.onLlcpActive(this);
			}
			if (messageToSend instanceof Symmetry) {
				for (LlcpSocket llcpSocket : openConnections.values()) {
					llcpSocket.onConnectionActive();
					messageToSend = handleMessageToSend(llcpSocket);
				}
			}
		}
		return messageToSend;
	}

	private void handlePendingConnectionTimeout() {
		for (Iterator<PendingConnection> it = pendingConnections.values().iterator(); it.hasNext();) {
			PendingConnection pc = it.next();
			long waitingTime = System.currentTimeMillis() - pc.getConnectionStart();
			if (waitingTime > linkTimeOut) {
				if (pc.getRetries() > MAX_RETRIES) {
					pc.getServiceAccessPoint().onConnectFailed();
					it.remove();
				}
				else {
					pc.incRetries();
					if (log.isDebugEnabled())
						log.debug("Retrying connect " + pc.getRetries() + " - Waiting time: " + waitingTime);
					messageToSend = pc.getConnectPdu();
					return;
				}
			}
		}
	}

	public AbstractProtocolDataUnit onConnectComplete(int remoteAddress, int localAddress, Object[] parameters) {
		log.info("connect complete, remote: " + remoteAddress + " lA: " + localAddress);
		Integer pendingLocalAddress = Integer.valueOf(localAddress);
		if (pendingConnections.containsKey(pendingLocalAddress)) {
			PendingConnection pendingConnection = pendingConnections.remove(pendingLocalAddress);
			LlcpSocket llcpSocket = new LlcpSocket(new AddressPair(remoteAddress, localAddress),
					pendingConnection.getServiceAccessPoint());
			openConnections.put(pendingLocalAddress, llcpSocket);
			aggreeOnMiux(parameters, llcpSocket);
			llcpSocket.onConnectSucceeded();
			return handleMessageToSend(llcpSocket);
		}
		else
			return new Disconnect(remoteAddress, localAddress); // TODO Frame reject
	}

	private AbstractProtocolDataUnit handleMessageToSend(LlcpSocket llcpSocket) {
		AbstractProtocolDataUnit pdu = llcpSocket.getMessageToSend();
		return pdu;
	}

	@Override
	public void connectToService(String serviceName, ServiceAccessPoint serviceAccessPoint) {
		// TODO move this to llcpsocket
		Object[] parameter = { new ServiceName(serviceName) };
		// TODO MIUX
		int outgoingAddress = getFreeOutgoingAddress();
		Connect connectPdu = new Connect(SERVICE_DISCOVERY_ADDRESS, outgoingAddress, parameter);
		pendingConnections.put(outgoingAddress, new PendingConnection(serviceAccessPoint, System.currentTimeMillis(),
				connectPdu));
		messageToSend = connectPdu;
	}

	@Override
	public void connectToService(int serviceAddress, ServiceAccessPoint serviceAccessPoint) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	private LlcpSocket getOpenLlcpSocket(AddressPair addressPair) {
		return getLlcpSocketFromCollection(addressPair, openConnections.values());
	}

	private LlcpSocket getLlcpSocketFromCollection(AddressPair addressPair, Collection<LlcpSocket> sockets) {
		for (Iterator<LlcpSocket> iterator = sockets.iterator(); iterator.hasNext();) {
			LlcpSocket llcpSocket = iterator.next();
			if (llcpSocket.equalsAddress(addressPair)) {
				return llcpSocket;
			}
		}
		log.info("Socket not found for rA: " + addressPair.getRemote() + " lA: " + addressPair.getRemote());
		return null;
	}

	public AbstractProtocolDataUnit onSendConfirmed(int remoteAddress, int localAddress, int receivedSequence) {
		LlcpSocket llcpSocket = getOpenLlcpSocket(new AddressPair(remoteAddress, localAddress));
		llcpSocket.onSendConfirmed(receivedSequence);
		return handleMessageToSend(llcpSocket);
	}

	public AbstractProtocolDataUnit onConnect(int remoteAddress, int localAddress, Object[] parameters) {
		ServiceAccessPoint serviceAccessPoint = getServiceAccessPoint(localAddress,
				LlcpUtils.getServiceNameFromParameters(parameters));
		if (serviceAccessPoint == null) {
			return new DisconnectedMode(remoteAddress, localAddress, 2);
		}
		else {
			if (serviceAccessPoint.canAcceptConnection(parameters)) {
				Integer outgoingAddress = getFreeOutgoingAddress();
				LlcpSocket llcpSocket = new LlcpSocket(new AddressPair(remoteAddress, outgoingAddress),
						serviceAccessPoint);
				openConnections.put(outgoingAddress, llcpSocket);
				int aggreeOnMiux = aggreeOnMiux(parameters, llcpSocket);
				List<Object> parameter = new ArrayList<Object>(getParameter());
				if (aggreeOnMiux != 0)
					parameter.add(new Miux(aggreeOnMiux));
				return new ConnectComplete(remoteAddress, outgoingAddress, parameter.toArray());
			}
			else {
				return new DisconnectedMode(remoteAddress, localAddress, 3);
			}
		}
	}

	private int aggreeOnMiux(Object[] parameters, LlcpSocket llcpSocket) {
		int remoteMiux = LlcpUtils.getMiuExtension(parameters);
		int aggreeOnMiux = Math.min(miuExtension, remoteMiux);
		llcpSocket.setMaximumInformationUnitExtension(aggreeOnMiux);
		return aggreeOnMiux;
	}

	public void init(Object[] parameters) {
		for (Object param : parameters) {
			if (param instanceof Version) {
				Version version = (Version)param;
				log.info("LLCP Version: " + version.getMajor() + "." + version.getMinor());
				if (version.getMajor() != VERSION_MAJOR) {
					throw new RuntimeException("Cannot handle Version " + version.getMajor());
				}
				else if (version.getMinor() == 0) {
					oldAndroidQuirck();
				}
			}
			else if (param instanceof Miux) {
				Miux miux = (Miux)param;
				miuExtension = Math.min(PREFERRED_MIUX, miux.getValue());
				log.info("LLCP Miux: " + miux.getValue() + ", agreed on " + miuExtension);
			}
			else if (param instanceof LinkTimeOut) {
				LinkTimeOut lto = (LinkTimeOut)param;
				linkTimeOut = lto.getValue() * 10;
				log.info("LLCP Link Timeout: " + linkTimeOut + " ms");
			}
		}
		log.info("All params parsed: " + parameters.length);
	}

	private void oldAndroidQuirck() {
		try {
			log.info("Waiting...");
			Thread.sleep(100);
			log.info("Done waiting.");
		}
		catch (InterruptedException e) {
		}
	}

	public int getOpenConnectionsSize() {
		return openConnections.size();
	}

	public AbstractProtocolDataUnit onReceiveInformation(int remoteAddress, int localAddress, int received, int send,
			byte[] serviceDataUnit) {
		LlcpSocket llcpSocket = getOpenLlcpSocket(new AddressPair(remoteAddress, localAddress));
		llcpSocket.onInformation(received, send, serviceDataUnit);
		return handleMessageToSend(llcpSocket);
	}

	private void closeSocket(LlcpSocket llcpSocket) {
		openConnections.values().remove(llcpSocket);
	}

	public AbstractProtocolDataUnit onDisconnect(int remoteAddress, int localAddress) {
		LlcpSocket llcpSocket = getOpenLlcpSocket(new AddressPair(remoteAddress, localAddress));
		closeSocket(llcpSocket);
		llcpSocket.onDisconnect();
		return handleMessageToSend(llcpSocket);
	}

	public AbstractProtocolDataUnit onDisconnectedMode(int remoteAddress, int localAddress, int reason) {
		LlcpSocket llcpSocket = null;
		switch (reason) {
			case 0x00: // disc OK
				llcpSocket = getOpenLlcpSocket(new AddressPair(remoteAddress, localAddress));
				if (llcpSocket != null) {
					log.info("Closing open connection");
					closeSocket(llcpSocket);
					llcpSocket.onDisconnectSucceeded();
					return handleMessageToSend(llcpSocket);
				}
			case 0x01:
				llcpSocket = getOpenLlcpSocket(new AddressPair(remoteAddress, localAddress));
				if (llcpSocket != null) {
					log.info("Closing open connection");
					closeSocket(llcpSocket);
					llcpSocket.onDisconnect();
					return new Symmetry();
				}
			case 0x02: // no service bound
			case 0x03: // rejected by service layer
			case 0x10: // perm not accept connection at same target point
			case 0x11: // perm not accept connection at any target point
				if (pendingConnections.containsKey(localAddress)) {
					Integer pendingLocalAddress = Integer.valueOf(localAddress);
					log.info("Closing pending connection");
					PendingConnection pendingConnection = pendingConnections.remove(pendingLocalAddress);
					pendingConnection.getServiceAccessPoint().onConnectFailed();
				}
				return new Symmetry();
			case 0x21: // temp not accept connection at same target point
			case 0x20: // temp not accept connection at any target point
				// TODO add additional timeout for this messages
				return new Symmetry();
			default:
				return new Symmetry();
		}
	}
}

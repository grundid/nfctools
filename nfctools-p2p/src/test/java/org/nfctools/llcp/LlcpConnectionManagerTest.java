package org.nfctools.llcp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.llcp.parameter.ServiceName;
import org.nfctools.llcp.pdu.AbstractProtocolDataUnit;
import org.nfctools.llcp.pdu.Connect;
import org.nfctools.llcp.pdu.ConnectComplete;
import org.nfctools.llcp.pdu.Disconnect;
import org.nfctools.llcp.pdu.DisconnectedMode;
import org.nfctools.llcp.pdu.DummyServiceAccessPoint;
import org.nfctools.llcp.pdu.Information;
import org.nfctools.llcp.pdu.ReceiveReady;
import org.nfctools.llcp.pdu.Symmetry;

public class LlcpConnectionManagerTest {

	private static final String COM_ANDROID_NPP = "com.android.npp";
	private LlcpConnectionManager connectionManager = new LlcpConnectionManager();

	@Test
	public void testSymmetryOnIdle() throws Exception {
		AbstractProtocolDataUnit protocolDataUnit = connectionManager.onLlcpActive();
		assertTrue(protocolDataUnit instanceof Symmetry);
	}

	@Test
	public void testConnectUnavailableService() throws Exception {
		Connect connect = new Connect(0x01, 0x32, new ServiceName(COM_ANDROID_NPP));
		AbstractProtocolDataUnit processPdu = connect.processPdu(connectionManager);
		assertTrue(processPdu instanceof DisconnectedMode);
		assertEquals(2, ((DisconnectedMode)processPdu).getReason());
	}

	@Test
	public void testConnectUnavailableAddress() throws Exception {
		Connect connect = new Connect(0x02, 0x32, new ServiceName(COM_ANDROID_NPP));
		AbstractProtocolDataUnit processPdu = connect.processPdu(connectionManager);
		assertTrue(processPdu instanceof DisconnectedMode);
		assertEquals(2, ((DisconnectedMode)processPdu).getReason());
	}

	@Test
	public void testConnectWellKnownServiceWithServiceUnavailable() throws Exception {
		DummyServiceAccessPoint serviceAccessPoint = new DummyServiceAccessPoint(COM_ANDROID_NPP);
		serviceAccessPoint.setAcceptConnections(false);
		connectionManager.registerWellKnownServiceAccessPoint(COM_ANDROID_NPP, serviceAccessPoint);
		Connect connect = new Connect(0x01, 0x32, new ServiceName(COM_ANDROID_NPP));
		AbstractProtocolDataUnit processPdu = connect.processPdu(connectionManager);
		assertTrue(processPdu instanceof DisconnectedMode);
		assertEquals(3, ((DisconnectedMode)processPdu).getReason());
	}

	@Test
	public void testConnectWellKnownService() throws Exception {
		connectionManager.registerWellKnownServiceAccessPoint(COM_ANDROID_NPP, new DummyServiceAccessPoint());
		Connect connect = new Connect(0x01, 0x32, new ServiceName(COM_ANDROID_NPP));
		AbstractProtocolDataUnit processPdu = connect.processPdu(connectionManager);
		assertTrue(processPdu instanceof ConnectComplete);
	}

	@Test
	public void testWellKnownServiceConnectWithSendAndDisconnect() throws Exception {
		DummyServiceAccessPoint serviceAccessPoint = new DummyServiceAccessPoint("Hello", COM_ANDROID_NPP);
		connectionManager.registerWellKnownServiceAccessPoint(COM_ANDROID_NPP, serviceAccessPoint);
		AbstractProtocolDataUnit processPdu = new Symmetry().processPdu(connectionManager);
		assertTrue(processPdu.toString(), processPdu instanceof Connect);
		assertEquals(0, connectionManager.getOpenConnectionsSize());

		Connect connect = (Connect)processPdu;
		assertEquals(1, connect.getDestinationServiceAccessPoint());
		assertEquals(32, connect.getSourceServiceAccessPoint());

		processPdu = new ConnectComplete(connect.getSourceServiceAccessPoint(),
				connect.getDestinationServiceAccessPoint()).processPdu(connectionManager);

		assertEquals(1, connectionManager.getOpenConnectionsSize());
		assertTrue(processPdu.toString(), processPdu instanceof Information);

		Information information = (Information)processPdu;
		assertEquals("Hello", new String(information.getServiceDataUnit()));
		processPdu = new ReceiveReady(information.getSourceServiceAccessPoint(),
				information.getDestinationServiceAccessPoint(), 1).processPdu(connectionManager);
		assertTrue(processPdu.toString(), processPdu instanceof Disconnect);

		Disconnect disconnect = (Disconnect)processPdu;
		processPdu = new DisconnectedMode(disconnect.getSourceServiceAccessPoint(),
				disconnect.getDestinationServiceAccessPoint(), 0).processPdu(connectionManager);

		assertEquals(0, connectionManager.getOpenConnectionsSize());
		assertFalse(serviceAccessPoint.isConnected());
		assertSymmetryState();
	}

	@Test
	public void testWellKnownServiceConnectWithReceiveAndDisconnect() throws Exception {
		DummyServiceAccessPoint serviceAccessPoint = new DummyServiceAccessPoint(COM_ANDROID_NPP);
		connectionManager.registerWellKnownServiceAccessPoint(COM_ANDROID_NPP, serviceAccessPoint);
		AbstractProtocolDataUnit processPdu = new Connect(1, 32, new ServiceName(COM_ANDROID_NPP))
				.processPdu(connectionManager);
		assertTrue(processPdu.toString(), processPdu instanceof ConnectComplete);
		assertEquals(1, connectionManager.getOpenConnectionsSize());

		processPdu = new Information(32, 32, 0, 0, "Hello".getBytes()).processPdu(connectionManager);
		assertTrue(processPdu.toString(), processPdu instanceof ReceiveReady);
		ReceiveReady receiveReady = (ReceiveReady)processPdu;
		assertEquals(1, receiveReady.getReceived());
		assertEquals(0, receiveReady.getSend());

		processPdu = new Disconnect(32, 32).processPdu(connectionManager);
		assertTrue(processPdu.toString(), processPdu instanceof DisconnectedMode);

		assertEquals(0, connectionManager.getOpenConnectionsSize());
		assertFalse(serviceAccessPoint.isConnected());
		assertEquals("Hello", serviceAccessPoint.getMessageReceived());
		assertSymmetryState();
	}

	private void assertSymmetryState() {
		for (int x = 0; x < 10; x++) {
			AbstractProtocolDataUnit processPdu = new Symmetry().processPdu(connectionManager);
			assertTrue(processPdu.toString(), processPdu instanceof Symmetry);
		}
	}
}

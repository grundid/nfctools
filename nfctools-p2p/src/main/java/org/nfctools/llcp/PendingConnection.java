package org.nfctools.llcp;

import org.nfctools.llcp.pdu.Connect;

public class PendingConnection {

	private ServiceAccessPoint serviceAccessPoint;
	private long connectionStart;
	private int retries = 0;
	private Connect connectPdu;

	public PendingConnection(ServiceAccessPoint serviceAccessPoint, long connectionStart, Connect connectPdu) {
		this.serviceAccessPoint = serviceAccessPoint;
		this.connectionStart = connectionStart;
		this.connectPdu = connectPdu;
	}

	public ServiceAccessPoint getServiceAccessPoint() {
		return serviceAccessPoint;
	}

	public long getConnectionStart() {
		return connectionStart;
	}

	public void incRetries() {
		retries++;
	}

	public int getRetries() {
		return retries;
	}

	public Connect getConnectPdu() {
		return connectPdu;
	}
}

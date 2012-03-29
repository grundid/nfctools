package org.nfctools.llcp;

import java.util.HashMap;
import java.util.Map;

public class ServiceDiscovery {

	private Map<String, ServiceAccessPoint> services = new HashMap<String, ServiceAccessPoint>();

	public void registerSerivce(String serviceName, ServiceAccessPoint service) {
		services.put(serviceName, service);
	}

	public ServiceAccessPoint getService(String serviceName) {
		return services.get(serviceName);
	}

	public void onLlcpActive(Llcp llcp) {
		for (ServiceAccessPoint serviceAccessPoint : services.values()) {
			serviceAccessPoint.onLlcpActive(llcp);
		}
	}

}

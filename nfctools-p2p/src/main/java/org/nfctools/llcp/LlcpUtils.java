package org.nfctools.llcp;

import org.nfctools.llcp.parameter.Miux;
import org.nfctools.llcp.parameter.ServiceName;

public class LlcpUtils {

	public static String getServiceNameFromParameters(Object[] parameters) {
		if (parameters != null) {
			for (Object parameter : parameters) {
				if (parameter instanceof ServiceName) {
					return ((ServiceName)parameter).getName();
				}
			}
		}
		return null;
	}

	public static int getMiuExtension(Object[] parameters) {
		if (parameters != null) {
			for (Object parameter : parameters) {
				if (parameter instanceof Miux) {
					return ((Miux)parameter).getValue();
				}
			}
		}
		return 0;
	}
}

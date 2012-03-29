package org.nfctools;

import java.util.HashMap;
import java.util.Map;

public class NfcContext {

	public static final String KEY_COMMUNICATOR = "communicator";

	private Map<String, Object> contextMap = new HashMap<String, Object>();

	public void setAttribute(String key, Object value) {
		contextMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T)contextMap.get(key);
	}
}

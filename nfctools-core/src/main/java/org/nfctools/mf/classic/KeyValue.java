package org.nfctools.mf.classic;


public class KeyValue {

	private Key key;
	private byte[] keyValue;

	public KeyValue(Key key, byte[] keyValue) {
		this.key = key;
		this.keyValue = keyValue;
	}

	public Key getKey() {
		return key;
	}

	public byte[] getKeyValue() {
		return keyValue;
	}

}

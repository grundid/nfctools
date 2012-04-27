package org.nfctools.mf.mad;

import org.nfctools.mf.classic.Key;

public class MadKeyConfig {

	private Key createKey;
	private byte[] createKeyValue;
	private byte[] writeKeyValue;

	public MadKeyConfig(Key createKey, byte[] createKeyValue, byte[] writeKeyValue) {
		this.createKey = createKey;
		this.createKeyValue = createKeyValue;
		this.writeKeyValue = writeKeyValue;
	}

	public Key getCreateKey() {
		return createKey;
	}

	public byte[] getCreateKeyValue() {
		return createKeyValue;
	}

	public byte[] getWriteKeyValue() {
		return writeKeyValue;
	}

}

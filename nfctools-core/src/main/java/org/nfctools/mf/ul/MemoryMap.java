package org.nfctools.mf.ul;

import org.nfctools.mf.tlv.LockControlTlv;

public class MemoryMap {

	private byte[][] memory;
	private LockControlTlv dynamicLock;

	public MemoryMap(int pages) {
		memory = new byte[pages][4];
	}

	public void setPage(int pageNo, byte[] bytes) {
		for (int x = 0; x < 4; x++)
			memory[pageNo][x] = bytes[x];
	}

	public byte[] getPage(int pageNo) {
		return memory[pageNo];
	}

	public byte[][] getMemory() {
		return memory;
	}

	public LockControlTlv getDynamicLock() {
		return dynamicLock;
	}

	public void setDynamicLock(LockControlTlv dynamicLock) {
		this.dynamicLock = dynamicLock;
	}

	public boolean hasDynamicLock() {
		return dynamicLock != null;
	}
}

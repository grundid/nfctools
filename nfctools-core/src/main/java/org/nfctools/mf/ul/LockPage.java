package org.nfctools.mf.ul;

public class LockPage {

	private int page;
	private byte[] lockBytes;

	public LockPage(int page, byte[] lockBytes) {
		this.page = page;
		this.lockBytes = lockBytes;
	}

	public int getPage() {
		return page;
	}

	public byte[] getLockBytes() {
		return lockBytes;
	}

}

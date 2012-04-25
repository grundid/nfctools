package org.nfctools.mf.ul;

import org.nfctools.mf.tlv.LockControlTlv;

public class MemoryLayout {

	public static final MemoryLayout ULTRALIGHT = new MemoryLayout(
			new LockPage[] { new LockPage(2, new byte[] { 2, 3 }) }, 0, 15, 4, 15);
	public static final MemoryLayout ULTRALIGHT_C = new MemoryLayout(new LockPage[] {
			new LockPage(2, new byte[] { 2, 3 }), new LockPage(40, new byte[] { 0, 1 }) }, 0, 47, 4, 39);

	private LockPage[] lockPages;
	private int firstPage;
	private int lastPage;
	private int firstDataPage;
	private int lastDataPage;
	private int bytesPerPage = 4;
	private int capabilityPage = 3;
	private boolean dynamicLockBytes;

	private MemoryLayout(LockPage[] lockPages, int firstPage, int lastPage, int firstDataPage, int lastDataPage) {
		this.lockPages = lockPages;
		this.firstPage = firstPage;
		this.lastPage = lastPage;
		this.firstDataPage = firstDataPage;
		this.lastDataPage = lastDataPage;
		dynamicLockBytes = lockPages.length > 1;
	}

	public LockPage[] getLockPages() {
		return lockPages;
	}

	public int getFirstDataPage() {
		return firstDataPage;
	}

	public int getLastDataPage() {
		return lastDataPage;
	}

	public int getMaxSize() {
		return (lastDataPage - firstDataPage + 1) * bytesPerPage;
	}

	public int getBytesPerPage() {
		return bytesPerPage;
	}

	public int getCapabilityPage() {
		return capabilityPage;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public CapabilityBlock createCapabilityBlock() {
		return new CapabilityBlock((byte)0x10, (byte)(getMaxSize() / 8), false);
	}

	public boolean hasDynamicLockBytes() {
		return dynamicLockBytes;
	}

	public LockControlTlv createLockControlTlv() {
		if (hasDynamicLockBytes()) {
			LockControlTlv tlv = new LockControlTlv();
			tlv.setPageAddress(10);
			tlv.setByteOffset(0);
			tlv.setSize(16);
			tlv.setBytesPerPage(bytesPerPage);
			tlv.setBytesLockedPerLockBit(4);
			return tlv;
		}
		else {
			return null;
		}
	}

}

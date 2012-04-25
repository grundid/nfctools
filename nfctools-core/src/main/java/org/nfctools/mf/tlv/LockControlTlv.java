package org.nfctools.mf.tlv;

import org.nfctools.utils.NfcUtils;

public class LockControlTlv extends AbstractMemoryTlv {

	private int bytesLockedPerLockBit;

	public LockControlTlv() {
	}

	public LockControlTlv(byte[] bytes) {
		super(bytes);
		bytesLockedPerLockBit = NfcUtils.getMostSignificantNibble(bytes[2]);
	}

	@Override
	public byte[] toBytes() {
		byte[] bytes = super.toBytes();
		bytes[2] = NfcUtils.encodeNibbles(bytesLockedPerLockBit, bytesPerPage);
		return bytes;
	}

	public int getBytesLockedPerLockBit() {
		return bytesLockedPerLockBit;
	}

	public void setBytesLockedPerLockBit(int bytesLockedPerLockBit) {
		this.bytesLockedPerLockBit = bytesLockedPerLockBit;
	}

	public int getSizeInBytes() {
		return (int)Math.ceil(getSize() / 8);
	}

	/**
	 * It indicates the size in bits of the lock area i.e. the number of dynamic lock bits.
	 */
	@Override
	public int getSize() {
		return super.getSize();
	}
}

package org.nfctools.mf.tlv;

import org.nfctools.mf.MfUtils;

public class LockControlTlv extends AbstractMemoryTlv {

	private int bytesLockedPerLockBit;

	public LockControlTlv() {
	}

	public LockControlTlv(byte[] bytes) {
		super(bytes);
		bytesLockedPerLockBit = MfUtils.getMostSignificantNibble(bytes[2]);
	}

	public byte[] toBytes() {
		byte[] bytes = new byte[3];
		bytes[0] = MfUtils.encodeNibbles(pageAddress, byteOffset);
		bytes[1] = (byte)size;
		bytes[2] = MfUtils.encodeNibbles(bytesLockedPerLockBit, bytesPerPage);
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

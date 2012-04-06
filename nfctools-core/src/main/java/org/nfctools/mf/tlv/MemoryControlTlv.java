package org.nfctools.mf.tlv;

import org.nfctools.mf.MfUtils;

public class MemoryControlTlv extends AbstractMemoryTlv {

	public MemoryControlTlv() {
	}

	public MemoryControlTlv(byte[] bytes) {
		super(bytes);
	}

	public byte[] toBytes() {
		byte[] bytes = new byte[3];
		bytes[0] = MfUtils.encodeNibbles(pageAddress, byteOffset);
		bytes[1] = (byte)size;
		bytes[2] = MfUtils.encodeNibbles(0, bytesPerPage);
		return bytes;
	}
}

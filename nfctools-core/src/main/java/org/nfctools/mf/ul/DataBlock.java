package org.nfctools.mf.ul;

import org.nfctools.mf.block.MfBlock;

public class DataBlock implements MfBlock {

	protected byte[] data;

	public DataBlock(byte[] data) {
		this(data, 0);
	}

	public DataBlock(byte[] data, int offset) {
		this.data = new byte[4];
		System.arraycopy(data, offset, this.data, 0, 4);
	}

	@Override
	public byte[] getData() {
		return data;
	}

}

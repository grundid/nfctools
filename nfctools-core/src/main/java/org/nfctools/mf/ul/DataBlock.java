package org.nfctools.mf.ul;

import org.nfctools.mf.block.MfBlock;

public class DataBlock implements MfBlock {

	private byte[] data;

	public DataBlock(byte[] data) {
		this.data = data;
	}

	@Override
	public byte[] getData() {
		return data;
	}

}

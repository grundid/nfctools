package org.nfctools.spi.file;

import org.nfctools.mf.block.MfBlock;

public class SimpleBlock implements MfBlock {

	private byte[] data;

	public SimpleBlock(byte[] data) {
		this.data = data;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}

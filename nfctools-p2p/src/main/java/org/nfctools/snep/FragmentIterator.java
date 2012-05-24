package org.nfctools.snep;

import java.util.Iterator;

public class FragmentIterator implements Iterator<byte[]> {

	private byte[] data;
	private int maxFragmentSize;
	private int offset = 0;

	public FragmentIterator(byte[] data, int maxFragmentSize) {
		this.data = data;
		this.maxFragmentSize = maxFragmentSize;
	}

	@Override
	public boolean hasNext() {
		return offset < data.length;
	}

	@Override
	public byte[] next() {
		byte[] buffer = new byte[Math.min(data.length - offset, maxFragmentSize)];
		System.arraycopy(data, offset, buffer, 0, buffer.length);
		offset += buffer.length;
		return buffer;
	}

	@Override
	public void remove() {
	}
}

package org.nfctools.tags;

import java.io.OutputStream;

import org.nfctools.ndef.NotEnoughMemoryException;

public class TagOutputStream extends OutputStream {

	private byte[] buffer;
	private int count;

	public TagOutputStream(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}

		buffer = new byte[size];
	}

	public int getRemainingSize() {
		return buffer.length - count;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	@Override
	public void write(int b) {
		if (getRemainingSize() > 0) {
			buffer[count] = (byte)b;
			count++;
		}
		else
			throw new NotEnoughMemoryException();
	}

	@Override
	public void write(byte b[], int off, int len) {
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		}
		else if (len == 0) {
			return;
		}
		if (getRemainingSize() >= len) {
			System.arraycopy(b, off, buffer, count, len);
			count += len;
		}
		else
			throw new NotEnoughMemoryException();
	}
}

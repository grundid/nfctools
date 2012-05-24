package org.nfctools.snep;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FragmentReader {

	private int fragmentCount = 0;
	private long expectedLength;
	private ByteArrayOutputStream out = new ByteArrayOutputStream();

	public void addFragment(byte[] data) {
		try {
			if (out.size() == 0) {
				ByteBuffer byteBuffer = ByteBuffer.wrap(data);
				expectedLength = byteBuffer.getInt(2) + 6;
			}
			out.write(data);
			fragmentCount++;
		}
		catch (IOException e) {
		}
	}

	public void reset() {
		out.reset();
	}

	public boolean isComplete() {
		return out.size() == expectedLength;
	}

	public byte[] getCompleteMessage() {
		return out.toByteArray();
	}

	public boolean isFirstFragment() {
		return fragmentCount == 1;
	}
}

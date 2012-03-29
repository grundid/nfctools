package org.nfctools.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputOutputToken {

	private InputStream inputStream;
	private OutputStream outputStream;

	void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public InputStream getInputStream() {
		if (inputStream == null)
			throw new IllegalStateException("connection token not initialized");
		return inputStream;
	}

	public OutputStream getOutputStream() {
		if (outputStream == null)
			throw new IllegalStateException("connection token not initialized");
		return outputStream;
	}

	void close() throws IOException {
		getInputStream().close();
		getOutputStream().close();
		inputStream = null;
		outputStream = null;
	}

}

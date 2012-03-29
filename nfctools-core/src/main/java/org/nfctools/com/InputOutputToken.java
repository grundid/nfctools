/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

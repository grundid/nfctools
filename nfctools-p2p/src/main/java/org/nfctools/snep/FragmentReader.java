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

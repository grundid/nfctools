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
package org.nfctools.nfcip;

import java.io.IOException;

public interface NFCIPConnection {

	int MODE_INITIATOR = 1;
	int MODE_TARGET = 2;

	/**
	 * Maximum DEP transfer buffer.
	 */
	int NFCIP_BUFFER_SIZE = 251;

	boolean isTarget();

	boolean isInitiator();

	byte[] receive() throws IOException;

	void send(byte[] data) throws IOException;

	void close() throws IOException;

	byte[] getGeneralBytes();
}

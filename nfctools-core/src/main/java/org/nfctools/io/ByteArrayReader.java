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
package org.nfctools.io;

import java.io.IOException;

/**
 * Reader for messages. Every implementation MUST read as much data as is available in the source. If the given buffer
 * is too small an Exception MUST be thrown. If this reader understands the message only a complete message MUST be
 * returned. A read call MIGHT block until a complete and valid message is received.
 * 
 */
public interface ByteArrayReader {

	/**
	 * Set a negative number to disable timeout
	 * 
	 * @param millis
	 */
	void setTimeout(long millis);

	int read(byte[] data, int offset, int length) throws IOException;
}

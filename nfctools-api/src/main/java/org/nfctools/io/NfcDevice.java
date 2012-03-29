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
 * Interface for a physical NFC device.
 * 
 */
public interface NfcDevice {

	/**
	 * Performs any actions necessary to connect to the device and create the Input- and OutputStreams.
	 * 
	 * @throws IOException
	 */
	void open() throws IOException;

	/**
	 * Closes the device.
	 * 
	 * @throws IOException
	 */
	void close() throws IOException;

	Object getConnectionToken();
}

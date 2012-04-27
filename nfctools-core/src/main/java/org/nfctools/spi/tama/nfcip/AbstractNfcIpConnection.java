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
package org.nfctools.spi.tama.nfcip;

import org.nfctools.nfcip.NFCIPConnection;

public abstract class AbstractNfcIpConnection implements NFCIPConnection {

	private int mode;
	protected byte[] generalBytes;

	protected AbstractNfcIpConnection(int mode, byte[] generalBytes) {
		this.mode = mode;
		this.generalBytes = generalBytes;
	}

	@Override
	public boolean isTarget() {
		return mode == MODE_TARGET;
	}

	@Override
	public boolean isInitiator() {
		return mode == MODE_INITIATOR;
	}

	@Override
	public byte[] getGeneralBytes() {
		return generalBytes;
	}
}

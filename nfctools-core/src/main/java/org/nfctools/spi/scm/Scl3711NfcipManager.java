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
package org.nfctools.spi.scm;

import java.io.IOException;

import org.nfctools.SimpleNfcTarget;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPConnectionListener;
import org.nfctools.nfcip.NFCIPManager;

public class Scl3711NfcipManager implements NFCIPManager {

	private Scl3711 scl3711;

	public Scl3711NfcipManager(Scl3711 scl3711) {
		this.scl3711 = scl3711;
	}

	@Override
	public NFCIPConnection connectAsTarget() throws IOException {
		throw new IllegalStateException("unsupported operation");
	}

	@Override
	public NFCIPConnection connectAsInitiator() throws IOException {
		while (!Thread.interrupted()) {
			try {
				ConnectResponse connectResponse = scl3711.initiatorConnect(LlcpConstants.nfcId3t,
						LlcpConstants.initiatorGeneralBytes);
				SimpleNfcTarget target = new SimpleNfcTarget(0, connectResponse.getNfcId(),
						connectResponse.getGeneralBytes());
				return new Scl3711Initiator(scl3711, target);
			}
			catch (TimeoutException e) {
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException ie) {
					return null;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				scl3711.disconnect();
			}
		}
		return null;
	}

	@Override
	public void initAsTarget() throws IOException {
		throw new IllegalStateException("unsupported operation");

	}

	@Override
	public void setConnectionListener(NFCIPConnectionListener connectionListener) {
		throw new IllegalStateException("unsupported operation");
	}

}

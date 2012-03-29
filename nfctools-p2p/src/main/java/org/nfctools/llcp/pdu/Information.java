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
package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class Information extends AbstractSequenceProtocolDataUnit {

	private byte[] serviceDataUnit;

	public Information(int destinationServiceAccessPoint, int sourceServiceAccessPoint, int received, int send,
			byte[] serviceDataUnit) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint, received, send);
		this.serviceDataUnit = serviceDataUnit;
	}

	public byte[] getServiceDataUnit() {
		return serviceDataUnit;
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		return connectionManager.onReceiveInformation(getSourceServiceAccessPoint(),
				getDestinationServiceAccessPoint(), getReceived(), getSend(), serviceDataUnit);
	}
}

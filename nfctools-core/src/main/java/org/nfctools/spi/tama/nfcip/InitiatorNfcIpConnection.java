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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.nfctools.nfcip.NFCIPCommunicator;
import org.nfctools.spi.tama.request.DataExchangeReq;
import org.nfctools.spi.tama.request.ReleaseReq;
import org.nfctools.spi.tama.response.DataExchangeResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitiatorNfcIpConnection extends AbstractNfcIpConnection {

	private Logger log = LoggerFactory.getLogger(getClass());

	private NFCIPCommunicator tamaCommunicator;
	private int targetId;
	private ByteArrayOutputStream response = new ByteArrayOutputStream();

	public InitiatorNfcIpConnection(NFCIPCommunicator tamaCommunicator, byte[] generalBytes, int targetId) {
		super(MODE_INITIATOR, generalBytes);
		this.tamaCommunicator = tamaCommunicator;
		this.targetId = targetId;
	}

	public int getTargetId() {
		return targetId;
	}

	//	@Override
	//	public void setTimeout(long millis) {
	//		tamaCommunicator.setTimeout(millis);
	//	}

	@Override
	public byte[] receive() throws IOException {
		//		boolean targetHasMoreInformation = false;
		//		do {
		//			log.debug("Receiving data...");
		//			DataExchangeResp dataExchangeResponse = tamaCommunicator.sendMessage(new DataExchangeReq(targetId, false,
		//					new byte[0], 0, 0));
		//
		//			response.write(dataExchangeResponse.getDataOut());
		//			targetHasMoreInformation = dataExchangeResponse.isMoreInformation();
		//
		//			log.debug("Received: " + dataExchangeResponse.getDataOut().length + " target MI: "
		//					+ targetHasMoreInformation);
		//
		//		} while (targetHasMoreInformation);

		byte[] result = response.toByteArray();
		response.reset();
		return result;
	}

	@Override
	public void send(byte[] data) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		byte[] buffer = new byte[NFCIP_BUFFER_SIZE];
		do {
			int dataRead = in.read(buffer, 0, buffer.length);
			boolean moreInformationToSend = buffer.length == dataRead && in.available() > 0;

			if (dataRead < 0) // if data was empty send 0 bytes
				dataRead = 0;

			log.debug("Sending data... " + dataRead + " MI: " + moreInformationToSend);
			DataExchangeResp dataExchangeResponse = tamaCommunicator.sendMessage(new DataExchangeReq(targetId,
					moreInformationToSend, buffer, 0, dataRead));
			if (dataExchangeResponse.getDataOut().length > 0)
				response.write(dataExchangeResponse.getDataOut());
			//				throw new IOException("unexpected data received " + dataExchangeResponse.getDataOut().length);
		} while (in.available() > 0);
	}

	@Override
	public void close() throws IOException {
		tamaCommunicator.sendMessage(new ReleaseReq(getTargetId()));
	}

}

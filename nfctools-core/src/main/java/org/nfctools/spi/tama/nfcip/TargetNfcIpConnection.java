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
import org.nfctools.spi.tama.request.GetDepDataReq;
import org.nfctools.spi.tama.request.SetDepDataReq;
import org.nfctools.spi.tama.request.SetMetaDepDataReq;
import org.nfctools.spi.tama.response.GetDepDataResp;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetNfcIpConnection extends AbstractNfcIpConnection {

	private Logger log = LoggerFactory.getLogger(getClass());

	private NFCIPCommunicator tamaCommunicator;

	public TargetNfcIpConnection(TamaNfcIpCommunicator tamaCommunicator, byte[] generalBytes) {
		super(MODE_TARGET, generalBytes);
		this.tamaCommunicator = tamaCommunicator;
	}

	@Override
	public byte[] receive() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		do {
			GetDepDataResp getDepDataResp = tamaCommunicator.sendMessage(new GetDepDataReq());
			out.write(getDepDataResp.getDataIn(), 0, getDepDataResp.getDataIn().length);
			log.debug("Data received: " + (getDepDataResp.getDataIn().length) + " More Information: "
					+ getDepDataResp.isMoreInformation());
			log.debug("Received data: " + NfcUtils.convertBinToASCII(getDepDataResp.getDataIn()));

			if (!getDepDataResp.isMoreInformation()) {
				//				tamaCommunicator.sendMessage(new SetDepDataReq(new byte[0], 0, 0));
				break;
			}
		} while (true);
		return out.toByteArray();
	}

	@Override
	public void send(byte[] data) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		byte[] sendBuffer = new byte[NFCIP_BUFFER_SIZE];
		log.debug("Data to send: " + in.available());

		//		GetDepDataResp getDepDataResp = tamaCommunicator.sendMessage(new GetDepDataReq());
		//		if (getDepDataResp.getDataIn().length != 0)
		//			throw new IOException("unexpected data received");

		if (in.available() == 0) {
			tamaCommunicator.sendMessage(new SetDepDataReq(sendBuffer, 0, 0));
		}
		else {

			while (in.available() > 0) {

				int dataRead = in.read(sendBuffer, 0, sendBuffer.length);

				boolean moreInformationToSend = sendBuffer.length == dataRead && in.available() > 0;
				log.debug("Sending data... " + dataRead + " more to send: " + moreInformationToSend);
				log.debug("Send data: " + NfcUtils.convertBinToASCII(sendBuffer, 0, dataRead));

				if (moreInformationToSend)
					tamaCommunicator.sendMessage(new SetMetaDepDataReq(sendBuffer, 0, dataRead));
				else {
					tamaCommunicator.sendMessage(new SetDepDataReq(sendBuffer, 0, dataRead));
				}
			}
		}
	}

	@Override
	public void close() throws IOException {
		// Don't know what to do here.
	}

}

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
package org.nfctools.ndefpush;

import java.io.IOException;

import org.nfctools.NfcContext;
import org.nfctools.api.Target;
import org.nfctools.api.TargetListener;
import org.nfctools.llcp.pdu.PduDecoder;
import org.nfctools.spi.tama.nfcip.TamaNfcIpCommunicator;
import org.nfctools.spi.tama.request.SetGeneralBytesReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LlcpTargetListener implements TargetListener {

	private Logger log = LoggerFactory.getLogger(getClass());
	private byte[] llcHeader = { 0x46, 0x66, 0x6D };
	private byte[] llcParameters = {};

	public LlcpTargetListener(byte[] llcParameters) {
		this.llcParameters = llcParameters;
	}

	@Override
	public void onTarget(Target target, NfcContext nfcContext) throws IOException {

		log.info("New target");
		TamaNfcIpCommunicator nfcIpCommunicator = nfcContext.getAttribute(NfcContext.KEY_COMMUNICATOR);
		//		nfcIpCommunicator.setTimeout(100); // default
		//
		//		List<Object> parameter = extractLlcParameters(target.getNfcId());
		//		nfcContext.setAttribute("llcparameter", parameter);
		//		for (Object object : parameter) {
		//			if (object instanceof LinkTimeOut) {
		//				LinkTimeOut lto = (LinkTimeOut)object;
		//				nfcIpCommunicator.setTimeout(lto.getValue() * 10);
		//			}
		//		}

		// TODO check target general bytes for llc header

		byte[] generalBytes = new byte[llcHeader.length + llcParameters.length];
		System.arraycopy(llcHeader, 0, generalBytes, 0, llcHeader.length);
		System.arraycopy(llcParameters, 0, generalBytes, llcHeader.length, llcParameters.length);

		log.info("reply...");
		Integer statusCode = nfcIpCommunicator.sendMessage(new SetGeneralBytesReq(generalBytes));
		if (statusCode.intValue() != 0)
			throw new IOException("status code: " + statusCode);
	}

	protected Object[] extractLlcParameters(byte[] incomingGeneralBytes) {
		byte[] incomingLlcParameters = {};
		for (int x = 0; x <= incomingGeneralBytes.length - llcHeader.length; x++) {
			boolean match = true;
			for (int h = 0; h < llcHeader.length; h++) {
				if (incomingGeneralBytes[x + h] != llcHeader[h]) {
					match = false;
					break;
				}
			}
			if (match && incomingGeneralBytes.length > x + llcHeader.length) {
				incomingLlcParameters = new byte[incomingGeneralBytes.length - x - llcHeader.length];
				System.arraycopy(incomingGeneralBytes, x + llcHeader.length, incomingLlcParameters, 0,
						incomingLlcParameters.length);
			}
		}

		PduDecoder pduDecoder = new PduDecoder();
		Object[] parameter = pduDecoder.decodeParameter(incomingLlcParameters, 0);
		return parameter;
	}
}

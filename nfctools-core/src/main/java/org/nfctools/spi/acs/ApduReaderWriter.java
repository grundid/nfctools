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
package org.nfctools.spi.acs;

import java.io.IOException;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import org.nfctools.io.ByteArrayReader;
import org.nfctools.io.ByteArrayWriter;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class ApduReaderWriter implements ByteArrayReader, ByteArrayWriter {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Card card;
	private CardChannel cardChannel;
	private byte[] responseData;

	public ApduReaderWriter(Card card, boolean useBasicChannel) {
		this.card = card;
		if (useBasicChannel)
			cardChannel = card.getBasicChannel();
	}

	//	public void disableAutoPICCPolling() throws CardException {
	//		byte[] command = { (byte)0xff, 0x00, 0x51, 0x00, 0x00 };
	//
	//		responseAPDU = new ResponseAPDU(card.transmitControlCommand(Acs.IOCTL_SMARTCARD_ACR122_ESCAPE_COMMAND, command));
	//		//			if (!Apdu.isSuccess(responseAPDU))
	//		//				throw new IOException("Error sending message => ["
	//		//						+ NfcUtils.convertBinToASCII(responseAPDU.getBytes()) + "]");
	//		//			else
	//		//		System.out.println("Result: " + NfcUtils.convertBinToASCII(responseAPDU.getBytes()));
	//
	//	}
	//
	//	public void getStatus() throws IOException {
	//		byte[] command = { (byte)0xff, 0x00, 0x00, 0x02, (byte)0xd4, 0x04 };
	//		CommandAPDU commandAPDU = new CommandAPDU(0xff, 0, 0, 0, new byte[] { (byte)0xd4, 0x04 });
	//		sendMessageInternal(commandAPDU.getBytes());
	//	}
	//
	//	public void releaseTarget() throws IOException {
	//		CommandAPDU commandAPDU = new CommandAPDU(0xff, 0, 0, 0, new byte[] { (byte)0xd4, 0x52, 0x01 });
	//		sendMessageInternal(commandAPDU.getBytes());
	//	}
	//
	//	private void sendMessageInternal(byte[] command) throws IOException {
	//		try {
	//			responseAPDU = new ResponseAPDU(card.transmitControlCommand(Acs.IOCTL_SMARTCARD_ACR122_ESCAPE_COMMAND,
	//					command));
	//			if (!Apdu.isSuccess(responseAPDU))
	//				throw new IOException("Error sending message => ["
	//						+ NfcUtils.convertBinToASCII(responseAPDU.getBytes()) + "]");
	//			else
	//				System.out.println("Result: " + NfcUtils.convertBinToASCII(responseAPDU.getBytes()));
	//		}
	//		catch (CardException e) {
	//			throw new IOException(e);
	//		}
	//
	//	}

	@Override
	public void write(byte[] data, int offset, int length) throws IOException {
		try {
			CommandAPDU commandAPDU = new CommandAPDU(0xff, 0, 0, 0, data, offset, length);
			byte[] commandBytes = commandAPDU.getBytes();

			if (log.isDebugEnabled()) {
				log.debug("command  APDU => " + NfcUtils.convertBinToASCII(commandBytes));
			}

			ResponseAPDU responseAPDU = null;
			if (cardChannel != null) {
				responseAPDU = cardChannel.transmit(commandAPDU);
			}
			else {
				byte[] transmitControlResponse = card.transmitControlCommand(Acs.IOCTL_SMARTCARD_ACR122_ESCAPE_COMMAND,
						commandBytes);
				responseAPDU = new ResponseAPDU(transmitControlResponse);
			}

			responseData = responseAPDU.getData();
			if (log.isDebugEnabled())
				log.debug("response APDU <= " + NfcUtils.convertBinToASCII(responseData));

			if (!ApduUtils.isSuccess(responseAPDU))
				throw new ApduException("Error sending message [" + NfcUtils.convertBinToASCII(data) + "] (" + offset
						+ "," + length + ") => [" + NfcUtils.convertBinToASCII(responseData) + "]");
		}
		catch (CardException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void setTimeout(long millis) {
	}

	@Override
	public int read(byte[] data, int offset, int length) throws IOException {
		if (responseData.length > length - offset)
			throw new IllegalArgumentException("buffer too small for response, needed " + responseData.length
					+ " bytes");

		System.arraycopy(responseData, 0, data, offset, responseData.length);
		return responseData.length;
	}

}

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

import javax.smartcardio.Card;
import javax.smartcardio.CardException;

import org.nfctools.spi.acs.Acs;

@SuppressWarnings("restriction")
public class Scl3711 {

	/**
	 * Returns the type of the card detected by the reader.
	 */
	public static final int IOCTL_GET_CARD_TYPE = 0x900;

	/**
	 * Returns the supported target and initiator modes.
	 */
	public static final int IOCTL_GET_DEVICE_CAPAB = 0x901;
	/**
	 * Switches the device to Reader/Write or P2P mode.
	 */
	public static final int IOCTL_GET_OR_SET_RW_P2P_MODES = 0x906;

	/**
	 * Polls for target devices in P2P initiator mode
	 */
	public static final int IOCTL_INITIATOR_POLL = 0x902;
	/**
	 * Connects to a target device in P2P initiator mode
	 */
	public static final int IOCTL_INITIATOR_CONNECT = 0x903;
	/**
	 * Sends and receives data to/from target device
	 */
	public static final int IOCTL_INITIATOR_TRANSCEIVE = 0x904;
	/**
	 * Disconnects from target device
	 */
	public static final int IOCTL_INITIATOR_DISCONNECT = 0x905;

	/**
	 * Receive data from initiator in target mode
	 */
	public static final int IOCTL_TARGET_RECEIVE = 0x907;

	/**
	 * Send
	 */
	public static final int IOCTL_TARGET_SEND = 0x908;

	private Card card;

	public Scl3711(Card card) {
		this.card = card;
	}

	private int getErrorCodeFromException(Throwable e) {
		if (e == null)
			return Integer.MIN_VALUE;
		else {
			String msg = e.getMessage();
			if (msg.startsWith("Unknown error 0x")) {
				String error = msg.substring("Unknown error 0x".length());
				return Integer.parseInt(error, 16);
			}
			else
				return getErrorCodeFromException(e.getCause());
		}
	}

	public byte[] transmit(int ioControl, byte[] command) throws IOException {
		try {
			ioControl = Acs.MakeIoctl(ioControl);
			return card.transmitControlCommand(ioControl, command);
		}
		catch (CardException e) {
			int error = getErrorCodeFromException(e);
			switch (error) {
				case 121:
					throw new TimeoutException();
				case 31:
					throw new GeneralFailureException();
				default:
					throw new IOException("Error " + error);
			}
		}
	}

	public ConnectResponse initiatorConnect(byte[] nfcipId, byte[] generalBytes) throws IOException {
		byte[] buffer = new byte[3 + 10 + 2 + 1 + 48];
		buffer[0] = 0;
		buffer[1] = 2;
		buffer[2] = 3;
		System.arraycopy(nfcipId, 0, buffer, 3, 10);
		System.arraycopy(generalBytes, 0, buffer, 16, generalBytes.length);
		buffer[13] = 0x0;
		buffer[14] = 0x4;
		buffer[15] = (byte)generalBytes.length;

		byte[] result;
		result = transmit(IOCTL_INITIATOR_CONNECT, buffer);

		byte[] nfcIdTarget = new byte[10];
		byte[] generalBytesTarget = new byte[result[16]];

		System.arraycopy(result, 1, nfcIdTarget, 0, 10);
		System.arraycopy(result, 17, generalBytesTarget, 0, generalBytesTarget.length);

		ConnectResponse response = new ConnectResponse(result[15], nfcIdTarget, generalBytesTarget);
		return response;

	}

	public int getCardType() throws IOException {
		byte[] result = transmit(IOCTL_GET_CARD_TYPE, new byte[] {});
		return result[0];
	}

	public int getDeviceCapab() throws IOException {
		byte[] result = transmit(IOCTL_GET_DEVICE_CAPAB, new byte[] {});
		return (result[0] & 0xff) << 8 | (result[1] & 0xff);
	}

	public void disconnect() throws IOException {
		transmit(IOCTL_INITIATOR_DISCONNECT, new byte[] { 1 });
	}

	public byte[] transceive(byte[] data) throws IOException {
		return transmit(IOCTL_INITIATOR_TRANSCEIVE, data);
	}

}

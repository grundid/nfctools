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
package org.nfctools.spi.tama.response;

import java.util.ArrayList;
import java.util.List;

import org.nfctools.spi.tama.TamaException;
import org.nfctools.spi.tama.TamaUtils;
import org.nfctools.spi.tama.response.GetGeneralStatusResp.Target;
import org.nfctools.utils.NfcUtils;

public class TamaResponseDecoder {

	@SuppressWarnings("unchecked")
	public <T> T decodeMessage(byte[] message) throws TamaException {
		if (message[0] == (byte)0xD5) {
			byte[] payload = new byte[message.length - 2];
			System.arraycopy(message, 2, payload, 0, payload.length);
			int responseCode = byteAsInt(message[1]);
			switch (responseCode) {
				case 0x03:
					return (T)createGetFirmwareVersionResp(payload);
				case 0x05:
					return (T)createGetGeneralStatusResp(payload);
				case 0x41:
					return (T)createDataExchangeResp(payload);
				case 0x57:
					return (T)createJumpForDepResp(payload);
				case 0x8D:
					return (T)createInitTamaTargetResp(payload);
				case 0x87:
					return (T)createGetDepDataResp(payload);
				case 0x45: // inDeselect
				case 0x53: // inRelease
				case 0x55: // inSelect
				case 0x8F: // tgSetDEPData
				case 0x95: // tgSetMetaDEPData
				case 0x93: // tgSetGeneralBytes
				case 0x91: // tgResponseToInitiator
					return (T)handleStatusCode(payload);
				case 0x13: // setParameters
				case 0x33: // rfCommunication
					return (T)Integer.valueOf(0);
				case 0x4b:
					return (T)createInListPassiveTargetResponse(payload);
			}
			throw new TamaException("unknown response " + responseCode);
		}
		else {
			throw new TamaException("Frame identifier (0xD5) expected, got [" + NfcUtils.convertBinToASCII(message)
					+ "]");
		}
	}

	private InListPassiveTargetResp createInListPassiveTargetResponse(byte[] payload) {
		byte[] targetData = new byte[payload.length - 1];
		System.arraycopy(payload, 1, targetData, 0, targetData.length);
		return new InListPassiveTargetResp(payload[0], targetData);
	}

	private static int byteAsInt(byte b) {
		return b & 0xff;
	}

	private Integer handleStatusCode(byte[] payload) throws TamaException {
		int status = byteAsInt(payload[0]);
		TamaUtils.handleStatusCode(status);
		return Integer.valueOf(status);
	}

	private DataExchangeResp createDataExchangeResp(byte[] payload) throws TamaException {
		int statusByte = byteAsInt(payload[0]);
		handleStatusCode(payload);
		if (TamaUtils.isNADPresent(statusByte)) {
			// TODO add NAD handler
			throw new IllegalStateException("NAD in payload not supported yet");
		}
		byte[] data = new byte[payload.length - 1];
		System.arraycopy(payload, 1, data, 0, payload.length - 1);
		return new DataExchangeResp(TamaUtils.isMoreInformation(statusByte), data);
	}

	private JumpForDepResp createJumpForDepResp(byte[] payload) throws TamaException {
		handleStatusCode(payload);
		int targetId = byteAsInt(payload[1]);
		byte[] nfcId = new byte[10];
		System.arraycopy(payload, 2, nfcId, 0, nfcId.length);
		byte[] generalBytes = new byte[payload.length - 17];
		System.arraycopy(payload, 17, generalBytes, 0, generalBytes.length);
		return new JumpForDepResp(targetId, nfcId, byteAsInt(payload[12]), byteAsInt(payload[13]),
				byteAsInt(payload[14]), byteAsInt(payload[15]), byteAsInt(payload[16]), generalBytes);
	}

	private InitTamaTargetResp createInitTamaTargetResp(byte[] payload) {
		int mode = byteAsInt(payload[0]);
		byte[] initiatorCommand = new byte[payload.length - 1];
		System.arraycopy(payload, 1, initiatorCommand, 0, payload.length - 1);
		return new InitTamaTargetResp(mode, initiatorCommand);
	}

	private GetDepDataResp createGetDepDataResp(byte[] payload) throws TamaException {
		int statusByte = byteAsInt(payload[0]);
		handleStatusCode(payload);
		if (TamaUtils.isNADPresent(statusByte)) {
			// TODO add NAD handler
			throw new IllegalStateException("NAD in payload not supported yet");
		}
		byte[] dataIn = new byte[payload.length - 1];
		System.arraycopy(payload, 1, dataIn, 0, payload.length - 1);
		return new GetDepDataResp(TamaUtils.isMoreInformation(statusByte), dataIn);
	}

	private GetFirmwareVersionResp createGetFirmwareVersionResp(byte[] payload) {
		if (payload.length == 2) // PN531
			return new GetFirmwareVersionResp(byteAsInt(payload[0]), byteAsInt(payload[1]));
		else if (payload.length == 4) //PN532/PN533
			return new GetFirmwareVersionResp(byteAsInt(payload[0]), byteAsInt(payload[1]), byteAsInt(payload[2]),
					byteAsInt(payload[3]));
		else
			throw new RuntimeException("Cannot handle payload with length: " + payload.length);
	}

	private GetGeneralStatusResp createGetGeneralStatusResp(byte[] payload) {
		int lastError = byteAsInt(payload[0]);
		boolean externalRfDetected = payload[1] == 0x01;
		int numberOfTargets = byteAsInt(payload[2]);
		List<Target> targets = new ArrayList<GetGeneralStatusResp.Target>(2);
		for (int x = 0; x < numberOfTargets; x++) {
			targets.add(new Target(payload[3 + (x * 4)], payload[3 + (x * 4) + 1], payload[3 + (x * 4) + 2],
					payload[3 + (x * 4) + 3]));
		}
		int samStatus = byteAsInt(payload[3 + (4 * numberOfTargets)]);
		return new GetGeneralStatusResp(lastError, externalRfDetected, numberOfTargets, samStatus, targets);
	}
}

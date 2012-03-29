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


package org.nfctools.spi.arygon;

import java.util.HashMap;
import java.util.Map;

import org.nfctools.utils.NfcUtils;

public class MicroControllerErrorResolver {

	private static final Map<Integer, String> msg = new HashMap<Integer, String>();

	static {
		msg.put(0x00, "OK = no Error.");
		msg.put(0x01, "Ringbuffer, interrupt write overflow (TAMA side).");
		msg.put(0x02, "UART1 receiver framing or overrun error (TAMA side).");
		msg.put(0x03, "UART2 receiver framing or overrun error (host side).");
		msg.put(0x04, "TAMA receive packet checksum wrong (packet length or packet data).");
		msg.put(0x05, "Host receive packet checksum wrong (packet length or packet data).");
		msg.put(0x06, "Unknown mode-select command from host.");
		msg.put(0x07, "Ringbuffer, interrupt write overflow (host side).");
		msg.put(0x08, "One or more command parameter are out of range.");
		msg.put(0x09, "TAMA has detected an error at application level.");
		msg.put(0x0A, "No or wrong TAMA ack received after sending of TAMA command sequences.");
		msg.put(0x0B, "Wrong TAMA set baud rate response received after TAMA set baud rate command.");
		msg.put(0x0C, "Host or TAMA Command packet supervision Timer expired. Packet not complete within 1s.");
		msg.put(0x0D, "Ringbuffer write overflow (host side).");
		msg.put(0x0E, "No or wrong TAMA ack received after μC sends a command to TAMA.");
		msg.put(0x0F, "Host High level language checksum wrong.");
		msg.put(0x10, "Current block has no value block format (block format is corrupted).");
		msg.put(0x11, "Error during increment / decrement / copy value block.");
		msg.put(0x12, "Baudrate not supported with the current low speed (low power) crystal.");
		msg.put(0x13, "Internal EEPROM read after write failed.");
		msg.put(0x14, "Internal EEPROM checksum failed (warning message).");
		msg.put(0x15, "Internal EEPROM address is out of the allowed range.");
		msg.put(0x16, "Internal EEPROM login missing. Application has no write access authorization.");
		msg.put(0x17, "Internal EEPROM login PIN code wrong. Access denied.");
		msg.put(0x18, "Receive partylinebuffer overflow (from TAMA to μC).");
		msg.put(0x19, "Infomessage: No response available (partylinebuffer is empty).");
		msg.put(0x1A, "Optional LCD Busy check supervision Timer expired.");
		msg.put(0x1B, "More than 3 EEPROM login retry with a wrong PIN code.");
		msg.put(0x2D, "Error during Mifare Ultralight C 3DES authentication.");
	}

	public static String resolveErrorMessage(ArygonMessage arygonMessage) {
		if (arygonMessage.hasErrorCodes()) {
			return "Code: " + arygonMessage.getErrorCode1() + ", "
					+ msg.get(new Integer(arygonMessage.getErrorCode1())) + " ["
					+ new String(NfcUtils.convertBinToASCII(arygonMessage.getHeader())) + "]";
		}
		else {
			return "unknown error code";
		}
	}
}

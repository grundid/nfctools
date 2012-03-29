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
package org.nfctools.spi.tama;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TamaException extends IOException {

	private int errorCode = -1;

	private static final Map<Integer, String> errorMessages = new HashMap<Integer, String>();

	// TODO create errorCode Resolver for this
	static {
		errorMessages.put(0x01, "Time out, the target has not answered");
		errorMessages.put(0x02, "A CRC error has been detected by the contactless UART");
		errorMessages.put(0x03, "A Parity error has been detected by the contactless UART");
		errorMessages.put(0x04, "During a MIFARE anticollision/select operation, "
				+ "an erroneoes Bit Count has been detected");
		errorMessages.put(0x05, "Framing error during MIFARE operation");
		errorMessages.put(0x06, "An abnormal bit-collision has been detected "
				+ "during bit wise anticollision at 106 kbps");
		errorMessages.put(0x07, "Communication buffer size insufficient");
		errorMessages.put(0x09, "RF Buffer overflow has been detected by the contactless UART");
		errorMessages.put(0x0a, "In active communication mode, the RF field has not been "
				+ "switched on in time the counterpart (as definied in NFCIP-1 standard)");
		errorMessages.put(0x0b, "RF Protocol error");
		errorMessages.put(0x0d, "Temperature error");
		errorMessages.put(0x0e, "Internal buffer overflow");
		errorMessages.put(0x10, "Invalid parameter");
		errorMessages.put(0x12, "DEP Protocol: unsupported command from the initiator");
		errorMessages.put(0x13, "DEP Protocol/Mifare/ISO-14443-4: The data format does not match the specification");
		errorMessages.put(0x14, "Mifare: Authentification error");
		errorMessages.put(0x24, "ISO 14443-3: UID Check byte wrong");
		errorMessages.put(0x25, "DEP Protocol: Invalid device state");
		errorMessages.put(0x26, "Operation not allowed in the configuration");
		errorMessages.put(0x27, "This command is not acceptable due to the current context of the PN531");
		errorMessages.put(0x29, "The PN531 configured as a target has been released by its initiator");

	};

	public TamaException(int errorCode) {
		this.errorCode = errorCode;
	}

	public TamaException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		String message = errorMessages.get(errorCode);
		if (message != null)
			return "0x" + Integer.toHexString(errorCode) + ": " + message;
		else if (message == null && errorCode != -1)
			return "Unknown error code: 0x" + Integer.toHexString(errorCode);
		else
			return super.getMessage();
	}
}

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

import gnu.io.SerialPort;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.nfctools.com.AbstractBaudRateNegotiator;

public class ArygonBaudRateNegotiator extends AbstractBaudRateNegotiator {

	private static Map<Integer, String> baudRateToArygonCodeMap = new TreeMap<Integer, String>();

	static {
		baudRateToArygonCodeMap.put(9600, "00");
		baudRateToArygonCodeMap.put(19200, "01");
		baudRateToArygonCodeMap.put(38400, "02");
		baudRateToArygonCodeMap.put(57600, "03");
		baudRateToArygonCodeMap.put(115200, "04");
		baudRateToArygonCodeMap.put(230400, "05");
		baudRateToArygonCodeMap.put(460800, "06");
	}

	@Override
	public void negotiateBaudRateOnObject(SerialPort port, int baudRate) throws IOException {

		String arygonCode = baudRateToArygonCodeMap.get(baudRate);
		if (arygonCode == null)
			throw new IllegalArgumentException("baudrate not supported.");

		this.port = port;
		in = port.getInputStream();
		out = port.getOutputStream();

		findoutCurrentBaudRate();

		log.debug("Setting baud rate between µC and TAMA to " + baudRate);
		sendMessageAndProcessResponse("0at" + arygonCode);

		log.debug("Setting baud rate between host and µC to " + baudRate);
		sendMessageAndProcessResponse("0ah" + arygonCode);

		setSerialPortParams(baudRate);

		checkPortBaudRate(); // Fix, Code 0x06
		clearInputBuffers();
	}

	private void sendMessageAndProcessResponse(String message) throws IOException {
		sendASCIIMessage(message);
		String resp = readResponse();
		if (!resp.startsWith("FF000000"))
			throw new MicroControllerException(resp);

		log.debug("resp: " + resp);
	}

	@Override
	protected boolean checkPortBaudRate() throws IOException {
		log.debug("checking baud rate ...");
		sendASCIIMessage("0av");
		String resp = readResponse();
		if (resp.startsWith("FF0000")) {
			return true;
		}
		else {
			return false;
		}
	}
}

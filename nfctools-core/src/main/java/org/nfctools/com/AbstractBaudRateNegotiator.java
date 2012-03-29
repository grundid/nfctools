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
package org.nfctools.com;

import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBaudRateNegotiator {

	protected final static int TIMEOUT = 1000;
	protected final static int BUFFER_SIZE = 1024;

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected SerialPort port = null;
	protected InputStream in = null;
	protected OutputStream out = null;

	public static final int BAUDRATE_9600 = 9600;
	public static final int BAUDRATE_115200 = 115200;
	public static final int BAUDRATE_460800 = 460800;

	protected int[] knownBaudRates = { BAUDRATE_9600, BAUDRATE_115200, BAUDRATE_460800 };

	public abstract void negotiateBaudRateOnObject(SerialPort port, int baudRate) throws IOException;

	protected abstract boolean checkPortBaudRate() throws IOException;

	protected String readResponse() throws IOException {
		waitForData();
		byte[] buffer = new byte[BUFFER_SIZE];
		int pos = 0;
		while (in.available() > 0 && pos < buffer.length) {
			pos += in.read(buffer, pos, buffer.length - pos);
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
			}
		}
		String resp = new String(buffer, 0, pos);
		log.debug("bytes received: " + pos + " => " + resp);
		return resp;
	}

	protected void clearInputBuffers() {
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesCleared = 0;
		try {
			while (in.available() > 0) {
				bytesCleared += in.read(buffer, 0, buffer.length);
				try {
					Thread.sleep(1);
				}
				catch (InterruptedException e) {
					break;
				}
			}
		}
		catch (IOException e) {
		}
		log.debug(bytesCleared + " bytes cleared");
	}

	protected void findoutCurrentBaudRate() throws IOException {
		if (checkPortBaudRate()) {
			log.trace("Current baud rate was default");
			return;
		}
		else {

			for (int baudRate : knownBaudRates) {
				clearInputBuffers();
				log.debug("Probing baud rate " + baudRate);
				setSerialPortParams(baudRate);

				if (checkPortBaudRate()) {
					log.trace("Current baud rate was " + baudRate);
					return;
				}
			}
			throw new RuntimeException("cannot identify current baud rate");
		}
	}

	protected void sendASCIIMessage(String s) throws IOException {
		log.debug(s);
		out.write(s.getBytes());
	}

	protected void setSerialPortParams(int baudRate) {
		log.debug("Setting com port baud rate to " + baudRate);
		try {
			port.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			try {
				Thread.sleep(60);
			}
			catch (InterruptedException e) {
			}
		}
		catch (UnsupportedCommOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private void waitForData() throws IOException {
		try {
			int counter = 0;
			while ((in.available() == 0) && (counter < TIMEOUT)) {
				Thread.sleep(1);
				counter++;
			}
			if (in.available() == 0) {
				throw new IOException("Timeout. No data received.");
			}
		}
		catch (InterruptedException e) {
			throw new IOException(e.getMessage());
		}
	}
}

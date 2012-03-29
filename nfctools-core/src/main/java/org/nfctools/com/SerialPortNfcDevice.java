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

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.nfctools.io.NfcDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialPortNfcDevice implements NfcDevice {

	private final static int TIMEOUT_FOR_OPEN = 1000;
	private final static int SERIAL_PORT_BUFFER_SIZE = 1024;

	private Logger log = LoggerFactory.getLogger(getClass());

	private SerialPort port = null;
	private AbstractBaudRateNegotiator speedNegotiator;
	private InputOutputToken inputOutputToken = new InputOutputToken();

	private int baudRate;
	private String comPort;

	public SerialPortNfcDevice(AbstractBaudRateNegotiator speedNegotiator) {
		this.speedNegotiator = speedNegotiator;
	}

	@Override
	public InputOutputToken getConnectionToken() {
		return inputOutputToken;
	}

	@Override
	public void close() throws IOException {
		inputOutputToken.close();
		port.close();
		port = null;
	}

	@Override
	public void open() throws IOException {
		if (port == null) {
			try {
				CommPortIdentifier identifier = findCommPortIdentifier(comPort);
				if (identifier == null) {
					throw new RuntimeException("ComPort not found: " + comPort);
				}

				port = (SerialPort)identifier.open(SerialPortNfcDevice.class.getName() + "." + comPort,
						TIMEOUT_FOR_OPEN);

				port.setInputBufferSize(SERIAL_PORT_BUFFER_SIZE);
				//				initSerialPortEventListener();

				log.trace(port + " BaudRate: " + port.getBaudRate() + ", InputBuffer: " + port.getInputBufferSize());

				tweakPort();

				inputOutputToken.setInputStream(port.getInputStream());
				inputOutputToken.setOutputStream(port.getOutputStream());

				speedNegotiator.negotiateBaudRateOnObject(port, baudRate);

				log.trace("Comport opened: " + port + " BaudRate: " + port.getBaudRate() + ", InputBuffer: "
						+ port.getInputBufferSize());
			}
			catch (PortInUseException e) {
				throw new IOException(e);
			}
		}
	}

	private void initSerialPortEventListener() throws IOException {
		try {
			port.addEventListener(new SerialPortEventListenerImpl());
			port.notifyOnDataAvailable(true);
		}
		catch (TooManyListenersException e) {
			throw new IOException(e);
		}
	}

	private void tweakPort() {
		try {
			port.enableReceiveThreshold(1);
			port.enableReceiveTimeout(10);
		}
		catch (UnsupportedCommOperationException e) {
			throw new RuntimeException(e);
		}

		log.trace("ReceiveThreshold: " + port.getReceiveThreshold() + " ReceiveTimeout: " + port.getReceiveTimeout()
				+ " enabled: " + port.isReceiveTimeoutEnabled() + " DataBits: " + port.getDataBits() + " StopBits: "
				+ port.getStopBits() + " Parity: " + port.getParity());
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	@SuppressWarnings("unchecked")
	private CommPortIdentifier findCommPortIdentifier(String name) {
		Enumeration<CommPortIdentifier> enumeration = CommPortIdentifier.getPortIdentifiers();

		CommPortIdentifier port = null;

		while (enumeration.hasMoreElements()) {
			CommPortIdentifier identifier = enumeration.nextElement();
			if (identifier.getName().equalsIgnoreCase(name)) {
				port = identifier;
			}
		}
		return port;
	}

}

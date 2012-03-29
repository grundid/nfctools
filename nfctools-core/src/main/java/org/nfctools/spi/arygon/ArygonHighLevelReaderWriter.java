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

import java.io.IOException;

import org.nfctools.io.NfcDevice;

public class ArygonHighLevelReaderWriter extends AbstractArygonReaderWriter {

	private ArygonAsciiReader arygonAsciiReader;
	private int bufferSize;

	public ArygonHighLevelReaderWriter(NfcDevice nfcDevice, int bufferSize) {
		super(nfcDevice);
		this.bufferSize = bufferSize;
	}

	@Override
	public void open() throws IOException {
		super.open();
		arygonAsciiReader = new ArygonAsciiReader(in, bufferSize);
	}

	public void sendMessage(ArygonMessage message) throws IOException {
		log.debug(new String(message.getPayload()));
		out.write(message.getPayload());
		getMicroControllerResponse();
	}

	public ArygonMessage receiveMessage() throws IOException {
		return getTamaResponse();
	}

	public String getReaderVersion() throws IOException {
		out.write("0av".getBytes());
		return new String(getMicroControllerResponse().getPayload());
	}

	public void initLeds() throws IOException {
		for (LED led : LED.values()) {
			sendMessage(("0apc" + led.getId() + "00").getBytes());

		}
	}

	public boolean hasData() throws IOException {
		return arygonAsciiReader.hasData();
	}

	public void switchOnLed(LED led) throws IOException {
		sendMessage(("0apw" + led.getId() + "01").getBytes());
	}

	public void switchOffLed(LED led) throws IOException {
		sendMessage(("0apw" + led.getId() + "00").getBytes());
	}

	public void sendMessage(byte[] message) throws IOException {
		sendMessage(new ArygonMessage(message));
	}

	private ArygonMessage getTamaResponse() throws IOException {
		ArygonMessage message = getMicroControllerResponse();
		return message;
	}

	private ArygonMessage getMicroControllerResponse() throws IOException {
		ArygonMessage message = arygonAsciiReader.readResponse();
		if (message.hasHeader() && message.hasErrorCodes())
			throw new MicroControllerException(MicroControllerErrorResolver.resolveErrorMessage(message));
		else {
			return message;
		}
	}
}

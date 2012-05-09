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

import org.nfctools.api.Target;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scl3711Initiator implements NFCIPConnection {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Scl3711 scl3711;
	private Target target;
	private byte[] buffer;

	public Scl3711Initiator(Scl3711 scl3711, Target target) {
		this.scl3711 = scl3711;
		this.target = target;
	}

	@Override
	public boolean isTarget() {
		return false;
	}

	@Override
	public boolean isInitiator() {
		return true;
	}

	@Override
	public byte[] receive() throws IOException {
		if (buffer.length != 2 && buffer[0] != 0 && log.isTraceEnabled())
			log.trace("receive => " + NfcUtils.convertBinToASCII(buffer));
		return buffer;
	}

	@Override
	public void send(byte[] data) throws IOException {
		if (data.length != 2 && data[0] != 0 && log.isTraceEnabled())
			log.trace("send    => " + NfcUtils.convertBinToASCII(data));
		buffer = scl3711.transceive(data);
	}

	@Override
	public void close() throws IOException {
		scl3711.disconnect();
	}

	//	@Override
	//	public void setTimeout(long millis) {
	//		throw new IllegalStateException();
	//	}

	@Override
	public byte[] getGeneralBytes() {
		return target.getGeneralBytes();
	}
}

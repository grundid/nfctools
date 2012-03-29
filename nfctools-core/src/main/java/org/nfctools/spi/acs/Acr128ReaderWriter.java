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

import javax.smartcardio.CardChannel;

import org.nfctools.io.NfcDevice;
import org.nfctools.mf.MfAccess;

public class Acr128ReaderWriter extends AcsReaderWriter {

	public static final String TERMINAL_NAME = "ACS ACR128U PICC Interface";

	public Acr128ReaderWriter(NfcDevice nfcDevice) {
		super(nfcDevice);
		if (!cardTerminal.getName().contains(TERMINAL_NAME))
			throw new IllegalArgumentException("card terminal not supported");
	}

	@Override
	protected void loginIntoSector(MfAccess mfAccess, CardChannel cardChannel) throws IOException {
		super.loginIntoSector(mfAccess, cardChannel, Acs.P2_SESSION_KEY);
	}

}

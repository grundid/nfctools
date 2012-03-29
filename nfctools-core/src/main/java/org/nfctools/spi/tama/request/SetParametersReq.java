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
package org.nfctools.spi.tama.request;

@SuppressWarnings("unused")
public class SetParametersReq {

	private static final int NAD_USED = 0x01;
	private static final int DID_USED = 0x02;
	private static final int AUTOMATIC_ATR_RES = 0x04;
	private static final int TDA_POWERED = 0x08;
	private static final int AUTOMATIC_RATS = 0x10;
	private static final int SECURE = 0x20;

	private int flags = AUTOMATIC_ATR_RES | AUTOMATIC_RATS;

	public SetParametersReq setAutomaticATR_RES(boolean value) {
		if (value) {
			flags |= AUTOMATIC_ATR_RES;
		}
		else {
			flags &= ~AUTOMATIC_ATR_RES;
		}
		return this;
	}

	public byte getFlags() {
		return (byte)flags;
	}

}

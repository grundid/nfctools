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

public class JumpForDepReq {

	private boolean active;
	private byte bautRate;
	private byte[] passiveInitiatorData;
	private byte[] nfcId3i;
	private byte[] generalBytes;

	public JumpForDepReq(boolean active, byte bautRate, byte[] nfcId3i) {
		this.active = active;
		this.bautRate = bautRate;
		this.nfcId3i = nfcId3i;
	}

	public JumpForDepReq(boolean active, byte bautRate, byte[] passiveInitiatorData, byte[] nfcId3i, byte[] generalBytes) {
		this.active = active;
		this.bautRate = bautRate;
		this.passiveInitiatorData = passiveInitiatorData;
		this.nfcId3i = nfcId3i;
		this.generalBytes = generalBytes;
	}

	public boolean isActive() {
		return active;
	}

	public byte getBautRate() {
		return bautRate;
	}

	public byte[] getPassiveInitiatorData() {
		return passiveInitiatorData;
	}

	public byte[] getNfcId3i() {
		return nfcId3i;
	}

	public byte[] getGeneralBytes() {
		return generalBytes;
	}

}

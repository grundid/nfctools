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
package org.nfctools.spi.tama.response;

public class JumpForDepResp {

	private int targetId;
	private byte[] nfcId;
	private int deviceIdTarget;
	private int sendBitRateTarget;
	private int receiveBitRateTarget;
	private int timeout;
	private int optionalParametersTarget;
	private byte[] generalBytes;

	public JumpForDepResp(int targetId, byte[] nfcId, int deviceIdTarget, int sendBitRateTarget,
			int receiveBitRateTarget, int timeout, int optionalParametersTarget, byte[] generalBytes) {
		this.targetId = targetId;
		this.nfcId = nfcId;
		this.deviceIdTarget = deviceIdTarget;
		this.sendBitRateTarget = sendBitRateTarget;
		this.receiveBitRateTarget = receiveBitRateTarget;
		this.timeout = timeout;
		this.optionalParametersTarget = optionalParametersTarget;
		this.generalBytes = generalBytes;
	}

	public int getTargetId() {
		return targetId;
	}

	public byte[] getNfcId() {
		return nfcId;
	}

	public int getDeviceIdTarget() {
		return deviceIdTarget;
	}

	public int getSendBitRateTarget() {
		return sendBitRateTarget;
	}

	public int getReceiveBitRateTarget() {
		return receiveBitRateTarget;
	}

	public int getTimeout() {
		return timeout;
	}

	public int getOptionalParametersTarget() {
		return optionalParametersTarget;
	}

	public byte[] getGeneralBytes() {
		return generalBytes;
	}

}

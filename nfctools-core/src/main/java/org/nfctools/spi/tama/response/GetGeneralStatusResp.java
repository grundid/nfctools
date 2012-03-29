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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetGeneralStatusResp {

	public static final String[] bitRates = { "106kbps", "212kbps", "424kbps" };
	public static final Map<Byte, String> modulationTypes = new HashMap<Byte, String>();
	static {
		modulationTypes.put((byte)0x00, "Mifare");
		modulationTypes.put((byte)0x01, "Active Mode");
		modulationTypes.put((byte)0x10, "Felica");
	}

	private int lastError;
	private boolean externalRfDetected;
	private int numberOfTargets;
	private int samStatus;
	private List<Target> targets = new ArrayList<Target>();

	public static class Target {

		private byte logicalNumber;
		private byte bitRateInReception;
		private byte bitRateInTransmission;
		private byte modulationType;

		public Target(byte logicalNumber, byte bitRateInReception, byte bitRateInTransmission, byte modulationType) {
			this.logicalNumber = logicalNumber;
			this.bitRateInReception = bitRateInReception;
			this.bitRateInTransmission = bitRateInTransmission;
			this.modulationType = modulationType;
		}

		public byte getBitRateInReception() {
			return bitRateInReception;
		}

		public byte getBitRateInTransmission() {
			return bitRateInTransmission;
		}

		public byte getLogicalNumber() {
			return logicalNumber;
		}

		public byte getModulationType() {
			return modulationType;
		}

		/**
		 * <p>
		 * Created by adrian, 02.11.2006
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("LogicalNumber: ").append(logicalNumber).append(" ");
			sb.append("BitRateInReception: ").append(bitRates[bitRateInReception]).append(" ");
			sb.append("BitRateInTransmission: ").append(bitRates[bitRateInTransmission]).append(" ");
			sb.append("ModulationType: ").append(modulationTypes.get(modulationType));
			return sb.toString();
		}
	}

	public GetGeneralStatusResp(int lastError, boolean externalRfDetected, int numberOfTargets, int samStatus,
			List<Target> targets) {
		this.lastError = lastError;
		this.externalRfDetected = externalRfDetected;
		this.numberOfTargets = numberOfTargets;
		this.samStatus = samStatus;
		this.targets = targets;
	}

	public boolean isExternalRfDetected() {
		return externalRfDetected;
	}

	public int getLastError() {
		return lastError;
	}

	public int getNumberOfTargets() {
		return numberOfTargets;
	}

	public int getSamStatus() {
		return samStatus;
	}

	public List<Target> getTargets() {
		return targets;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("LastError: ").append(lastError).append(" ");
		sb.append("externalRfDetected: ").append(externalRfDetected).append(" ");
		sb.append("NumberOfTargets: ").append(numberOfTargets).append(" ");
		for (Target target : targets) {
			sb.append("Target: [").append(target.toString()).append("] ");
		}
		sb.append("SamStatus: ").append(samStatus);

		return sb.toString();
	}
}

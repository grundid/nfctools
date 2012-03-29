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

import org.nfctools.spi.tama.TamaConstants;

public class InitTamaTargetReq {

	private boolean depOnly;
	private boolean passiveOnly;
	private byte[] mifareParams;
	private byte[] felicaParams;
	private byte[] nfcId3t;
	private byte[] generalBytes;

	public InitTamaTargetReq(boolean depOnly, boolean passiveOnly, byte[] mifareParams, byte[] felicaParams,
			byte[] nfcId3t, byte[] generalBytes) {
		if (mifareParams == null || mifareParams.length != TamaConstants.MIFARE_PARAM_LENGTH)
			throw new IllegalArgumentException("Mifare Params null or wrong length (should be 6)");
		if (felicaParams == null || felicaParams.length != TamaConstants.FELICA_PARAM_LENGTH)
			throw new IllegalArgumentException("Felica Params null or wrong length (should be 18)");
		if (nfcId3t == null || nfcId3t.length != TamaConstants.NFCID_PARAM_LENGTH)
			throw new IllegalArgumentException("nfcId3t Params null or wrong length (should be 10)");

		this.depOnly = depOnly;
		this.passiveOnly = passiveOnly;
		this.mifareParams = mifareParams;
		this.felicaParams = felicaParams;
		this.nfcId3t = nfcId3t;
		this.generalBytes = generalBytes;
	}

	public boolean isDepOnly() {
		return depOnly;
	}

	public boolean isPassiveOnly() {
		return passiveOnly;
	}

	public byte[] getMifareParams() {
		return mifareParams;
	}

	public byte[] getFelicaParams() {
		return felicaParams;
	}

	public byte[] getNfcId3t() {
		return nfcId3t;
	}

	public byte[] getGeneralBytes() {
		return generalBytes;
	}
}

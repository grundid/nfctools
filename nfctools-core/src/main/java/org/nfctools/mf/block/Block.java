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
package org.nfctools.mf.block;

import org.nfctools.mf.MfConstants;
import org.nfctools.utils.NfcUtils;

public abstract class Block implements MfBlock {

	public static int BLOCK_TYPE_VALUE = 0;
	public static int BLOCK_TYPE_DATA = 1;
	public static int BLOCK_TYPE_MANU = 2;
	public static int BLOCK_TYPE_TRAIL = 3;

	private static String[] typeNames = { "Value", "Data ", "Manu ", "Trail" };

	protected byte[] data;
	private int type;

	protected Block(byte[] data, int type) {
		this.type = type;
		if (data != null) {
			if (data.length == MfConstants.BYTES_PER_BLOCK) {
				this.data = data;
			}
			else {
				this.data = NfcUtils.convertASCIIToBin(new String(data));
			}
		}
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return "[" + typeNames[getType()] + "] [" + NfcUtils.convertBinToASCII(data) + "]";
	}

	@Override
	public byte[] getData() {
		return data;
	}

	public boolean isDataBlock() {
		return type == BLOCK_TYPE_DATA;
	}
}

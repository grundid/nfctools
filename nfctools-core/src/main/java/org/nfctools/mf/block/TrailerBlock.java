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
import org.nfctools.mf.MfException;
import org.nfctools.mf.classic.Key;

public class TrailerBlock extends Block {

	private static final int KEY_LENGTH = 6;

	private static final int KEY_A_INDEX = 0;
	private static final int KEY_B_INDEX = 10;
	private static final int GPB_INDEX = 9;
	private static final int ACCESS_CONDITIONS_INDEX = 6;

	public TrailerBlock() {
		super(new byte[MfConstants.BYTES_PER_BLOCK], BLOCK_TYPE_TRAIL);
		try {
			setAccessConditions(MfConstants.TRANSPORT_ACCESS_CONDITIONS);
			setKey(Key.A, MfConstants.TRANSPORT_KEY);
			setKey(Key.B, MfConstants.TRANSPORT_KEY);
			setGeneralPurposeByte(MfConstants.TRANSPORT_GPB);
		}
		catch (MfException e) {
		}
	}

	public TrailerBlock(byte data[]) throws MfException {
		super(data, BLOCK_TYPE_TRAIL);
		if (!validAccessConditions(getAccessConditions())) {
			throw new MfException("illegal trailer block");
		}
	}

	@Override
	public TrailerBlock clone() {
		try {
			byte[] cloneData = new byte[data.length];
			System.arraycopy(data, 0, cloneData, 0, data.length);
			return new TrailerBlock(cloneData);
		}
		catch (MfException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getKey(Key key) {
		byte[] keyValue = new byte[6];
		if (key == Key.A) {
			System.arraycopy(data, KEY_A_INDEX, keyValue, 0, KEY_LENGTH);
		}
		else {
			System.arraycopy(data, KEY_B_INDEX, keyValue, 0, KEY_LENGTH);
		}
		return keyValue;
	}

	public void setKey(Key key, byte[] keyValue) {
		if (key == Key.A) {
			System.arraycopy(keyValue, 0, data, KEY_A_INDEX, KEY_LENGTH);
		}
		else {
			System.arraycopy(keyValue, 0, data, KEY_B_INDEX, KEY_LENGTH);
		}
	}

	public byte getGeneralPurposeByte() {
		return data[GPB_INDEX];
	}

	public void setGeneralPurposeByte(byte gpb) {
		data[GPB_INDEX] = gpb;
	}

	public byte[] getAccessConditions() {
		byte[] acValue = new byte[3];
		System.arraycopy(data, ACCESS_CONDITIONS_INDEX, acValue, 0, acValue.length);
		return acValue;
	}

	public void setAccessConditions(byte[] acValue) throws MfException {
		if (validAccessConditions(acValue))
			System.arraycopy(acValue, 0, data, ACCESS_CONDITIONS_INDEX, acValue.length);
		else
			throw new MfException("illegal access conditions");
	}

	public static boolean validAccessConditions(byte[] acValue) {
		if (acValue.length != 3)
			return false;

		if ((((acValue[0] ^ 0xf0) >>> 4) & 0x0f) != (acValue[2] & 0x0f)) {
			return false;
		}

		if (((acValue[0] ^ 0x0f) & 0x0f) != ((acValue[1] >>> 4) & 0xf)) {
			return false;
		}

		if (((acValue[1] ^ 0x0f) & 0x0f) != ((acValue[2] >>> 4) & 0xf)) {
			return false;
		}
		return true;
	}

	public boolean isKeyBReadable() {
		int accessBits = extractAccesBitsForBlock(3);
		return accessBits == 0 || accessBits == 1 || accessBits == 2;
	}

	public boolean canWriteDataBlock(Key key, int dataArea) {
		int accessBits = extractAccesBitsForBlock(dataArea);
		if (Key.A.equals(key)) {
			return accessBits == 0;
		}
		else {
			return accessBits == 4 || accessBits == 3 || accessBits == 6 || (accessBits == 0 && !isKeyBReadable());
		}
	}

	public boolean canWriteTrailerBlock(Key key) {
		int accessBits = extractAccesBitsForBlock(3);
		if (Key.A.equals(key)) { // Simplified verification
			return accessBits == 1;
		}
		else {
			return accessBits == 3;
		}
	}

	private int extractAccesBitsForBlock(int blockId) {
		int accessBits = ((data[ACCESS_CONDITIONS_INDEX] >> blockId) & 0x01) << 2;
		accessBits |= ((data[ACCESS_CONDITIONS_INDEX + 2] >> blockId) & 0x01) << 1;
		accessBits |= ((data[ACCESS_CONDITIONS_INDEX + 1] >> blockId) & 0x01);

		int bitC2 = accessBits & 0x02;

		int inverted = (accessBits ^ 0x07);

		return (inverted & 0x05) | bitC2;

	}
}

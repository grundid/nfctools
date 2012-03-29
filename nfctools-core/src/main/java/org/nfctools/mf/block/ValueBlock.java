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
import org.nfctools.utils.NfcUtils;

public class ValueBlock extends Block {

	private int value;
	private byte address;

	public ValueBlock(byte[] data) throws MfException {
		super(data, BLOCK_TYPE_VALUE);
		decypherValueBlock(data);
	}

	public ValueBlock(int value, byte address) {
		super(null, BLOCK_TYPE_VALUE);
		this.data = cypherValueBlock(value, address);
		this.value = value;
		this.address = address;
	}

	/**
	 * Verifies if the given byte array has a valid value format.
	 * <p>
	 * Created by adrian, 29.10.2006
	 * 
	 * @param valueBlock
	 * @return
	 */
	public static boolean isValidValueBlock(byte[] valueBlock) {
		for (int x = 0; x < 4; x++) {
			if ((valueBlock[x] != ~valueBlock[x + 4]) || (valueBlock[x] != valueBlock[x + 8]))
				return false;
		}
		if ((valueBlock[12] != ~valueBlock[13]) || (valueBlock[12] != valueBlock[14])
				|| (valueBlock[13] != valueBlock[15]))
			return false;

		return true;
	}

	/**
	 * Converts the given byte array into a value and a address byte.
	 * <p>
	 * Created by adrian, 29.10.2006
	 * 
	 * @param valueBlock
	 * @throws MfException
	 */
	public void decypherValueBlock(byte[] valueBlock) throws MfException {
		for (int x = 0; x < 4; x++) {
			if ((valueBlock[x] != ~valueBlock[x + 4]) || (valueBlock[x] != valueBlock[x + 8]))
				throw new MfException("Not a valid value block. [" + NfcUtils.convertBinToASCII(valueBlock) + "]");
		}
		if ((valueBlock[12] != ~valueBlock[13]) || (valueBlock[12] != valueBlock[14])
				|| (valueBlock[13] != valueBlock[15]))
			throw new MfException("Not a valid value block. [" + NfcUtils.convertBinToASCII(valueBlock) + "]");

		value = NfcUtils.bytesToInt(valueBlock, 0);
		address = valueBlock[12];
	}

	/**
	 * Converts a value and an address into a valid value block.
	 * <p>
	 * Created by adrian, 29.10.2006
	 * 
	 * @param value
	 * @param address
	 * @return
	 */
	public byte[] cypherValueBlock(int value, byte address) {
		byte[] hexBin = NfcUtils.intTo4Bytes(value);
		byte[] bin = new byte[MfConstants.BYTES_PER_BLOCK];
		for (int x = 0; x < 4; x++) {
			// Store the value in the first four bytes
			bin[x] = hexBin[x];
			// Store the invers value in the next four bytes
			bin[x + 4] = (byte)~hexBin[x];
			// Store the value in the next four bytes
			bin[x + 8] = hexBin[x];
			// Store the address byte twice inverted and twice normal.
			bin[x + 12] = (x % 2 == 0) ? address : (byte)~address;
		}
		return bin;
	}

	public byte getAddress() {
		return address;
	}

	public void setAddress(byte address) {
		this.address = address;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}

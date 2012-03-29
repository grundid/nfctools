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


package org.nfctools.mf;

public class NxpCrc {

	public static final int CRC_PRESET = 0xc7; // docs example 0xc7; nokia: 0xe3

	private int crc = CRC_PRESET;

	public void add(int value) {
		/* x^8 + x^4 + x^3 + x^2 + 1 => 0x11d */
		int poly = 0x1d;

		crc ^= value;
		for (int currentBit = 7; currentBit >= 0; currentBit--) {
			long bitOut = crc & 0x80;
			crc <<= 1;
			if (bitOut > 0)
				crc ^= poly;
		}
	}

	public void reset() {
		crc = CRC_PRESET;
	}

	public byte getCrc() {
		return (byte)crc;
	}
}

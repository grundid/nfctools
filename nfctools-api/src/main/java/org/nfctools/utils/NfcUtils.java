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
package org.nfctools.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NfcUtils {

	public static byte[] convertHexAsciiToByteArray(byte[] bytes) {
		return convertHexAsciiToByteArray(bytes, 0, bytes.length);
	}

	public static byte[] convertHexAsciiToByteArray(byte[] bytes, int offset, int length) {
		byte[] bin = new byte[length / 2];
		for (int x = 0; x < length / 2; x++) {
			bin[x] = (byte)Integer.parseInt(new String(bytes, offset + (x * 2), 2), 16);
		}
		return bin;
	}

	public static byte[] convertASCIIToBin(String ascii) {
		byte[] bin = new byte[ascii.length() / 2];
		for (int x = 0; x < bin.length; x++) {
			bin[x] = (byte)Integer.parseInt(ascii.substring(x * 2, x * 2 + 2), 16);
		}
		return bin;
	}

	public static String convertBinToASCII(byte[] bin) {
		return convertBinToASCII(bin, 0, bin.length);
	}

	public static String convertBinToASCII(byte[] bin, int offset, int length) {
		StringBuilder sb = new StringBuilder();
		for (int x = offset; x < offset + length; x++) {
			String s = Integer.toHexString(bin[x]);

			if (s.length() == 1)
				sb.append('0');
			else
				s = s.substring(s.length() - 2);
			sb.append(s);
		}
		return sb.toString().toUpperCase();
	}

	public static byte[] intTo4Bytes(int i) {
		return new byte[] { (byte)((i >> 24) & 255), (byte)((i >> 16) & 255), (byte)((i >> 8) & 255), (byte)(i & 255) };
	}

	public static int bytesToInt(byte[] b, int offset) {
		return (b[offset + 0] & 255) << 24 | (b[offset + 1] & 255) << 16 | (b[offset + 2] & 255) << 8
				| (b[offset + 3] & 255);
	}

	public static int bytesAsciiToByte(byte[] b, int offset) {
		return ((b[offset] << 4 | b[offset + 1]) << 24) | ((b[offset + 2] << 4 | b[offset + 3]) << 16)
				| ((b[offset + 4] << 4 | b[offset + 5]) << 8) | ((b[offset + 6] << 4 | b[offset + 7]));
	}

	public static byte bytesToByte(byte[] b, int offset) {
		return (byte)(b[offset] << 4 | b[offset + 1]);
	}

	public static String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, "utf8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isEqualArray(byte[] b1, byte[] b2) {
		if (b1.length != b2.length)
			return false;

		for (int i = 0; i < b1.length; i++) {
			if (b1[i] != b2[i])
				return false;
		}
		return true;
	}

	public static byte[] getBytesFromStream(int length, ByteArrayInputStream bais) {
		byte[] bytes = new byte[length];
		bais.read(bytes, 0, bytes.length);
		return bytes;
	}

	public static byte getLeastSignificantNibble(byte data) {
		return (byte)(data & 0x0F);
	}

	public static byte getMostSignificantNibble(byte data) {
		return (byte)((data & 0xF0) >> 4);
	}

	public static byte encodeNibbles(int mostSignificantNibble, int leastSignificantNibble) {
		return (byte)((mostSignificantNibble & 0x0F) << 4 | (leastSignificantNibble & 0x0F));
	}
}

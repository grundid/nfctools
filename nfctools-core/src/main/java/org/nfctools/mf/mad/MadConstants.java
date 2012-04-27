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
package org.nfctools.mf.mad;

public class MadConstants {

	public static final int GPB_MAD_V1 = 0x01;
	public static final int GPB_MAD_V2 = 0x02;

	/**
	 * GPB for 2nd MAD sector trailer
	 */
	public static final byte GPB_MAD_2_TRAILER = 0x00;

	public static final int GPB_MAD_AVAILABLE = 0x80;
	public static final int GPB_MULTI_APP_CARD = 0x40;
	public static final int GPB_NDEF_CONFIG = GPB_MAD_AVAILABLE | GPB_MULTI_APP_CARD;

	public static final byte[] READ_WRITE_ACCESS_CONDITIONS = { 0x78, 0x77, (byte)0x88 };
	public static final byte[] READ_ONLY_ACCESS_CONDITIONS = { 0x07, (byte)0x8f, 0x0f };
	public static final byte[] DEFAULT_MAD_KEY = { (byte)0xA0, (byte)0xA1, (byte)0xA2, (byte)0xA3, (byte)0xA4,
			(byte)0xA5 };

}

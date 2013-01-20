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
package org.nfctools.spi.acs;

public class Apdu {

	public static final int MAX_EXPECTED_LENGTH = 256;
	public static final int MAX_EXPECTED_LENGTH_LONG = 65536;
	public static final int INS_ERASE_BINARY = 0x0E;
	public static final int INS_VERIFY = 0x20;
	public static final int INS_MANAGE_CHANNEL = 0x70;
	public static final int INS_EXTERNAL_AUTHENTICATE = 0x82;
	public static final int INS_GET_CHALLENGE = 0x84;
	public static final int INS_INTERNAL_AUTHENTICATE = 0x88;
	public static final int INS_INTERNAL_AUTHENTICATE_ACS = 0x86;
	public static final int INS_SELECT_FILE = 0xA4;
	public static final int INS_READ_BINARY = 0xB0;
	public static final int INS_READ_RECORDS = 0xB2;
	public static final int INS_GET_RESPONSE = 0xC0;
	public static final int INS_ENVELOPE = 0xC2;
	public static final int INS_GET_DATA = 0xCA;
	public static final int INS_WRITE_BINARY = 0xD0;
	public static final int INS_WRITE_RECORD = 0xD2;
	public static final int INS_UPDATE_BINARY = 0xD6;
	public static final int INS_PUT_DATA = 0xDA;
	public static final int INS_UPDATE_DATA = 0xDC;
	public static final int INS_APPEND_RECORD = 0xE2;
	public static final int CLS_PTS = 0xFF; // Class for PTS
}

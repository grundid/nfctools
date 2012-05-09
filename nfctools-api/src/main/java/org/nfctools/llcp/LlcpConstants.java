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
package org.nfctools.llcp;

import org.nfctools.api.ConnectionSetup;

public class LlcpConstants {

	public static final byte[] mifareParams = { 0x08, 0x00, 0x12, 0x34, 0x56, 0x40 };
	public static final byte[] felicaParams = { 0x01, (byte)0xfe, 0x01, 0x00, 0x00, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			(byte)0xff, (byte)0xff };
	public static final byte[] nfcId3t = { 0x01, (byte)0xfe, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	public static final byte[] generalBytes = new byte[] { 0x46, 0x66, 0x6D, 0x01, 0x01, 0x10 };
	public static final byte[] initiatorGeneralBytes = new byte[] { 0x46, 0x66, 0x6D, 0x01, 0x01, 0x10, 0x02, 0x02,
			0x00, 0x78, 0x04, 0x01, (byte)0x96 };

	public static final int DEFAULT_MIU = 128;

	public static final String COM_ANDROID_NPP = "com.android.npp";

	public static final ConnectionSetup CONNECTION_SETUP = new ConnectionSetup(mifareParams, felicaParams, nfcId3t,
			initiatorGeneralBytes);

}

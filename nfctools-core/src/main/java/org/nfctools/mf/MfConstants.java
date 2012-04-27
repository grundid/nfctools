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

import org.nfctools.mf.classic.Key;
import org.nfctools.mf.mad.ApplicationId;
import org.nfctools.mf.mad.MadKeyConfig;

public class MfConstants {

	public static final byte[] TRANSPORT_KEY = { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF };
	public static final byte[] TRANSPORT_ACCESS_CONDITIONS = { (byte)0xFF, 0x07, (byte)0x80 };
	public static final byte TRANSPORT_GPB = (byte)0x69;

	public static final byte[] NDEF_KEY = { (byte)0xD3, (byte)0xF7, (byte)0xD3, (byte)0xF7, (byte)0xD3, (byte)0xF7 };
	public static final byte[] NDEF_READ_WRITE_ACCESS_CONDITIONS = { 0x7F, 0x07, (byte)0x88 };
	public static final byte[] NDEF_READ_ONLY_ACCESS_CONDITIONS = { 0x07, (byte)0x8f, 0x0f };
	public static final byte NDEF_GPB_V10_READ_WRITE = 0x40;
	public static final byte NDEF_GPB_V10_READ_ONLY = 0x43;

	public static final byte NDEF_APPLICATION_CODE = (byte)0x03;
	public static final byte NDEF_FUNCTION_CLUSTER_CODE = (byte)0xe1;

	public static final int BYTES_PER_BLOCK = 16;

	public static final ApplicationId NDEF_APP_ID = new ApplicationId(MfConstants.NDEF_APPLICATION_CODE,
			MfConstants.NDEF_FUNCTION_CLUSTER_CODE);

	public static final MadKeyConfig NDEF_KEY_CONFIG = new MadKeyConfig(Key.A, TRANSPORT_KEY, NDEF_KEY);
}

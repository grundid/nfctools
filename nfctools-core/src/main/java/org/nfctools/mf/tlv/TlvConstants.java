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

package org.nfctools.mf.tlv;

import java.util.HashSet;
import java.util.Set;

public class TlvConstants {

	public static final int NULL_TLV = 0x00;
	public static final int LOCK_CONTROL_TLV = 0x01;
	public static final int MEMORY_CONTROL_TLV = 0x02;
	public static final int NDEF_TLV = 0x03;
	public static final int PROPRIETARY_TLV = 0xFD;
	public static final int TERMINATOR_TLV = 0xFE;

	public static Set<Integer> KNOWN_TLVS = new HashSet<Integer>();

	static {
		KNOWN_TLVS.add(LOCK_CONTROL_TLV);
		KNOWN_TLVS.add(MEMORY_CONTROL_TLV);
		KNOWN_TLVS.add(NDEF_TLV);
	}

}

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

import org.nfctools.api.TagType;
import org.nfctools.utils.NfcUtils;

public class AcsTagUtils {

	public static TagType identifyTagType(byte[] historicalBytes) {
		TagType tagType = TagType.UNKNOWN;
		if (historicalBytes.length >= 11) {
			int tagId = (historicalBytes[9] & 0xff) << 8 | historicalBytes[10];
			switch (tagId) {
				case 0x0001:
					return TagType.MIFARE_CLASSIC_1K;
				case 0x0002:
					return TagType.MIFARE_CLASSIC_4K;
				case 0x0003:
					return TagType.MIFARE_ULTRALIGHT;
				case 0x0026:
					return TagType.MIFARE_MINI;
				case 0xF004:
					return TagType.TOPAZ_JEWEL;
				case 0xF011:
					return TagType.FELICA_212K;
				case 0xF012:
					return TagType.FELICA_424K;
				case 0xFF40:
					return TagType.NFCIP;
			}
		}
		else
			System.out.println(NfcUtils.convertBinToASCII(historicalBytes));
		return tagType;
	}
}

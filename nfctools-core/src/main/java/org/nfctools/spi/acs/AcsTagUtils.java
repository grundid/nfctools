package org.nfctools.spi.acs;

import org.nfctools.api.TagType;
import org.nfctools.utils.NfcUtils;

public class AcsTagUtils {

	public static TagType identifyTagType(byte[] historicalBytes) {
		TagType tagType = TagType.UKNOWN;
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

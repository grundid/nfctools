package org.nfctools.mf.ul;

import org.nfctools.mf.block.MfBlock;

public class UltralightHandler {

	public static boolean isBlank(MfBlock[] blocks) {
		if (blocks[0].getData()[0] == 0x04) // manufacturer ID
		{
			if (isStaticallyUnlocked(blocks[2].getData()) && isOTPClear(blocks[3].getData())) {
				return matchesVersion(blocks[4].getData(), (byte)0x02, (byte)0x00)
						|| matchesVersion(blocks[4].getData(), (byte)0xff, (byte)0xff);
			}
		}
		return false;
	}

	private static boolean isOTPClear(byte[] data) {
		for (int x = 0; x < 4; x++) {
			if (data[x] != 0)
				return false;
		}
		return true;
	}

	public static boolean isStaticallyUnlocked(byte[] block) {
		return block[2] == 0 && block[3] == 0;
	}

	private static boolean matchesVersion(byte[] block, byte major, byte minor) {
		return block[0] == major && block[1] == minor;
	}

	public static boolean isFormatted(MfBlock[] blocks) {
		return isCapabilityContainerValid(blocks[3].getData());
	}

	private static boolean isCapabilityContainerValid(byte[] block) {
		return block[0] == (byte)0xE1 && block[1] == (byte)0x10 && block[2] >= (byte)0x06;
	}

}

package org.nfctools.spi.acs;

import javax.smartcardio.ResponseAPDU;

public class ApduUtils {

	public static boolean isSuccess(ResponseAPDU responseAPDU) {
		return responseAPDU.getSW1() == 0x90 && responseAPDU.getSW2() == 0x00;
	}
}

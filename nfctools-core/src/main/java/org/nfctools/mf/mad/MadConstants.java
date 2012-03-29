package org.nfctools.mf.mad;

public class MadConstants {

	public static final int GPB_MAD_V1 = 0x01;
	public static final int GPB_MAD_V2 = 0x02;

	public static final int GPB_MAD_AVAILABLE = 0x80;
	public static final int GPB_MULTI_APP_CARD = 0x40;

	public static final byte[] DEFAULT_MAD_ACCESS_CONDITIONS = { 0x78, 0x77, (byte)0x88 };
	public static final byte[] DEFAULT_MAD_KEY = { (byte)0xA0, (byte)0xA1, (byte)0xA2, (byte)0xA3, (byte)0xA4,
			(byte)0xA5 };

}

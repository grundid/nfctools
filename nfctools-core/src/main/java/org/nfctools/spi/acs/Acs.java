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

public class Acs {

	public static final int P1_LOAD_KEY_INTO_VOLATILE_MEM = 0x00;
	public static final int P1_LOAD_KEY_INTO_NON_VOLATILE_MEM = 0x20;

	public static final byte P2_SESSION_KEY = 0x20;

	public static final byte KEY_A = 0x60;
	public static final byte KEY_B = 0x61;

	//Linux: FILE_DEVICE_SMARTCARD = 0x42000000
	// IOCTL_X = FILE_DEVICE_SMARTCARD + X
	//Windows: 0x310000 + X * 4
	
	private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
	private static final int FILE_DEVICE_SMARTCARD_WINDOWS = 0x310000;
	private static final int FILE_DEVICE_SMARTCARD_LINUX = 0x42000000;

	public static final int MakeIoctl(int command) {
		int base = IS_WINDOWS ? FILE_DEVICE_SMARTCARD_WINDOWS : FILE_DEVICE_SMARTCARD_LINUX;
		base += IS_WINDOWS ? command * 4 : command;
		return base;
	}
	
	// Reader action IOCTLs
	public static final int IOCTL_SMARTCARD_DIRECT = MakeIoctl(2050);
	public static final int IOCTL_SMARTCARD_SELECT_SLOT = MakeIoctl(2051);
	public static final int IOCTL_SMARTCARD_DRAW_LCDBMP = MakeIoctl(2052);
	public static final int IOCTL_SMARTCARD_DISPLAY_LCD = MakeIoctl(2053);
	public static final int IOCTL_SMARTCARD_CLR_LCD = MakeIoctl(2054);
	public static final int IOCTL_SMARTCARD_READ_KEYPAD = MakeIoctl(2055);
	public static final int IOCTL_SMARTCARD_READ_RTC = MakeIoctl(2057);
	public static final int IOCTL_SMARTCARD_SET_RTC = MakeIoctl(2058);
	public static final int IOCTL_SMARTCARD_SET_OPTION = MakeIoctl(2059);
	public static final int IOCTL_SMARTCARD_SET_LED = MakeIoctl(2060);
	public static final int IOCTL_SMARTCARD_LOAD_KEY = MakeIoctl(2062);
	public static final int IOCTL_SMARTCARD_READ_EEPROM = MakeIoctl(2065);
	public static final int IOCTL_SMARTCARD_WRITE_EEPROM = MakeIoctl(2066);
	public static final int IOCTL_SMARTCARD_GET_VERSION = MakeIoctl(2067);
	public static final int IOCTL_SMARTCARD_GET_READER_INFO = MakeIoctl(2051);
	public static final int IOCTL_SMARTCARD_SET_CARD_TYPE = MakeIoctl(2060);
	public static final int IOCTL_SMARTCARD_ACR128_ESCAPE_COMMAND = MakeIoctl(2079);
	public static final int IOCTL_SMARTCARD_ACR122_ESCAPE_COMMAND = MakeIoctl(3500);

	public static final byte[] CMD_GET_FIRMWARE_VERSION = new byte[] { 0x18, 0x00 };
	public static final byte[] CMD_SET_BUZZER_DURATION = new byte[] { 0x28, 0x01 };
	public static final byte[] CMD_SET_LED_AND_BUZZER_BEHAVIOR = new byte[] { 0x21, 0x01 };
	public static final byte[] CMD_GET_LED_AND_BUZZER_BEHAVIOR = new byte[] { 0x21, 0x00 };

}

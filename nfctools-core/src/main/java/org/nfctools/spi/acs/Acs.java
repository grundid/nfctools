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

	public static final int FILE_DEVICE_SMARTCARD = 0x310000; // Reader action IOCTLs
	public static final int IOCTL_SMARTCARD_DIRECT = FILE_DEVICE_SMARTCARD + 2050 * 4;
	public static final int IOCTL_SMARTCARD_SELECT_SLOT = FILE_DEVICE_SMARTCARD + 2051 * 4;
	public static final int IOCTL_SMARTCARD_DRAW_LCDBMP = FILE_DEVICE_SMARTCARD + 2052 * 4;
	public static final int IOCTL_SMARTCARD_DISPLAY_LCD = FILE_DEVICE_SMARTCARD + 2053 * 4;
	public static final int IOCTL_SMARTCARD_CLR_LCD = FILE_DEVICE_SMARTCARD + 2054 * 4;
	public static final int IOCTL_SMARTCARD_READ_KEYPAD = FILE_DEVICE_SMARTCARD + 2055 * 4;
	public static final int IOCTL_SMARTCARD_READ_RTC = FILE_DEVICE_SMARTCARD + 2057 * 4;
	public static final int IOCTL_SMARTCARD_SET_RTC = FILE_DEVICE_SMARTCARD + 2058 * 4;
	public static final int IOCTL_SMARTCARD_SET_OPTION = FILE_DEVICE_SMARTCARD + 2059 * 4;
	public static final int IOCTL_SMARTCARD_SET_LED = FILE_DEVICE_SMARTCARD + 2060 * 4;
	public static final int IOCTL_SMARTCARD_LOAD_KEY = FILE_DEVICE_SMARTCARD + 2062 * 4;
	public static final int IOCTL_SMARTCARD_READ_EEPROM = FILE_DEVICE_SMARTCARD + 2065 * 4;
	public static final int IOCTL_SMARTCARD_WRITE_EEPROM = FILE_DEVICE_SMARTCARD + 2066 * 4;
	public static final int IOCTL_SMARTCARD_GET_VERSION = FILE_DEVICE_SMARTCARD + 2067 * 4;
	public static final int IOCTL_SMARTCARD_GET_READER_INFO = FILE_DEVICE_SMARTCARD + 2051 * 4;
	public static final int IOCTL_SMARTCARD_SET_CARD_TYPE = FILE_DEVICE_SMARTCARD + 2060 * 4;
	public static final int IOCTL_SMARTCARD_ACR128_ESCAPE_COMMAND = FILE_DEVICE_SMARTCARD + 2079 * 4;
	public static final int IOCTL_SMARTCARD_ACR122_ESCAPE_COMMAND = FILE_DEVICE_SMARTCARD + 3500 * 4;

	public static final byte[] CMD_GET_FIRMWARE_VERSION = new byte[] { 0x18, 0x00 };
	public static final byte[] CMD_SET_BUZZER_DURATION = new byte[] { 0x28, 0x01 };
	public static final byte[] CMD_SET_LED_AND_BUZZER_BEHAVIOR = new byte[] { 0x21, 0x01 };
	public static final byte[] CMD_GET_LED_AND_BUZZER_BEHAVIOR = new byte[] { 0x21, 0x00 };

}

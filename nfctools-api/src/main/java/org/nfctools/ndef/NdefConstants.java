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
package org.nfctools.ndef;

import java.nio.charset.Charset;

public class NdefConstants {

	/**
	 * Message Begin. The MB flag is a 1-bit field that when set indicates the start of an NDEF message.
	 */
	public static final int MB = 0x80;

	/**
	 * Message End. The ME flag is a 1-bit field that when set indicates the end of an NDEF message. Note, that in case
	 * of a chunked payload, the ME flag is set only in the terminating record chunk of that chunked payload.
	 */
	public static final int ME = 0x40;

	/**
	 * Chunk Flag. The CF flag is a 1-bit field indicating that this is either the first record chunk or a middle record
	 * chunk of a chunked payload.
	 */
	public static final int CF = 0x20;

	/**
	 * Short Record. The SR flag is a 1-bit field indicating, if set, that the PAYLOAD_LENGTH field is a single octet.
	 * This short record layout is intended for compact encapsulation of small payloads which will fit within PAYLOAD
	 * fields of size ranging between 0 to 255 octets.
	 */
	public static final int SR = 0x10;

	/**
	 * ID_LENGTH field is present. The IL flag is a 1-bit field indicating, if set, that the ID_LENGTH field is present
	 * in the header as a single octet. If the IL flag is zero, the ID_LENGTH field is omitted from the record header
	 * and the ID field is also omitted from the record.
	 */
	public static final int IL = 0x08;

	/**
	 * Type Name Format Mask. The TNF field value indicates the structure of the value of the TYPE field.
	 */
	public static final int TNF_MASK = 0x07;

	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	public static final byte[] EMPTY_NDEF_MESSAGE = { (byte)0xD0, 0x00, 0x00 };

	public static final byte TNF_EMPTY = 0;
	public static final byte TNF_WELL_KNOWN = 1;
	public static final byte TNF_MIME_MEDIA = 2;
	public static final byte TNF_ABSOLUTE_URI = 3;
	public static final byte TNF_EXTERNAL_TYPE = 4;
	public static final byte TNF_UNKNOWN = 5;
	public static final byte TNF_UNCHANGED = 6;
	public static final byte TNF_RESERVED = 7;

	public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");
	public static final Charset DEFAULT_CHARSET = Charset.forName("US-ASCII"); // http://www.ietf.org/rfc/rfc2046.txt point 4.1.2

}

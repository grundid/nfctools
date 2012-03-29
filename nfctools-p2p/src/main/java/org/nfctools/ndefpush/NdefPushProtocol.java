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
package org.nfctools.ndefpush;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.Record;

/**
 * Implementation of the NDEF Push Protocol.
 */
public class NdefPushProtocol {

	public static final byte ACTION_IMMEDIATE = (byte)0x01;
	public static final byte ACTION_BACKGROUND = (byte)0x02;

	private static final byte VERSION = 1;

	public static List<byte[]> parse(byte[] data) throws FormatException {

		List<byte[]> messages = new ArrayList<byte[]>();

		ByteArrayInputStream buffer = new ByteArrayInputStream(data);
		DataInputStream input = new DataInputStream(buffer);

		// Check version of protocol
		try {
			byte version = input.readByte();
			if (version != VERSION) {
				throw new FormatException("Got version " + version + ",  expected " + VERSION);
			}

			int numMessages = input.readInt();
			if (numMessages == 0) {
				throw new FormatException("Error while parsing NdefMessage");
			}

			// Read actions and messages
			byte[] actions = new byte[numMessages];
			for (int i = 0; i < numMessages; i++) {
				actions[i] = input.readByte();

				int messageLength = input.readInt();
				byte[] bytes = new byte[messageLength];
				int readLength = input.read(bytes);
				if (messageLength != readLength) {
					throw new FormatException("Error while parsing NdefMessage");
				}
				messages.add(bytes);

			}
		}
		catch (java.io.IOException e) {
			throw new FormatException("Error while parsing NdefMessage", e);
		}

		return messages;
	}

	public static byte[] toByteArray(Collection<Record> records) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
		DataOutputStream output = new DataOutputStream(buffer);
		NdefMessageEncoder ndefMessageEncoder = NdefContext.getNdefMessageEncoder();
		try {
			byte[] bytes = ndefMessageEncoder.encode(records);
			output.writeByte(VERSION);
			output.writeInt(1);
			output.writeByte(ACTION_IMMEDIATE);
			output.writeInt(bytes.length);
			output.write(bytes);
		}
		catch (java.io.IOException e) {
			return null;
		}

		return buffer.toByteArray();
	}
}
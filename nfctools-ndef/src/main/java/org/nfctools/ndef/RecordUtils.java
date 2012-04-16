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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.nfctools.ndef.wkt.records.Action;
import org.nfctools.ndef.wkt.records.GcActionRecord;
import org.nfctools.ndef.wkt.records.GcDataRecord;
import org.nfctools.ndef.wkt.records.GenericControlRecord;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndef.wkt.records.UriRecord;

public class RecordUtils {

	public static boolean equalsTarget(GenericControlRecord gcRecord, String targetNameOrUri) {
		if (gcRecord.getTarget().getTargetIdentifier() instanceof TextRecord) {
			return targetNameOrUri.equals(((TextRecord)gcRecord.getTarget().getTargetIdentifier()).getText());
		}
		else if (gcRecord.getTarget().getTargetIdentifier() instanceof UriRecord) {
			return targetNameOrUri.equals(((UriRecord)gcRecord.getTarget().getTargetIdentifier()).getUri());
		}
		else
			return false;
	}

	public static boolean equalsAction(GenericControlRecord gcRecord, String actionName) {
		GcActionRecord actionRecord = gcRecord.getAction();

		if (actionRecord.hasActionRecord() && actionRecord.getActionRecord() instanceof TextRecord) {
			return actionName.equals(((TextRecord)actionRecord.getActionRecord()).getText());
		}
		else
			return false;
	}

	public static boolean equalsAction(GenericControlRecord gcRecord, Action action) {
		GcActionRecord actionRecord = gcRecord.getAction();
		return (actionRecord.hasAction() && actionRecord.getAction().equals(action));
	}

	public static boolean hasRecordByKey(GcDataRecord dataRecord, String key) {
		for (Record record : dataRecord.getRecords()) {
			if (key.equals(record.getKey()))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Record> T getRecordByKey(GcDataRecord dataRecord, String key) {
		for (Record record : dataRecord.getRecords()) {
			if (key.equals(record.getKey()))
				return (T)record;
		}
		throw new IllegalArgumentException("record with the given key not found");
	}

	public static boolean isEqualArray(byte[] b1, byte[] b2) {
		if (b1.length != b2.length)
			return false;

		for (int i = 0; i < b1.length; i++) {
			if (b1[i] != b2[i])
				return false;
		}
		return true;
	}

	public static byte[] getBytesFromStream(int length, InputStream bais) {
		try {
			byte[] bytes = new byte[length];
			bais.read(bytes, 0, bytes.length);
			return bytes;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static int readUnsignedShort(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		if ((ch1 | ch2) < 0) {
			throw new EOFException();
		}
		return (short)((ch1 << 8) + (ch2 << 0));
	}

	public static byte[] readByteArray(InputStream in, int len) throws IOException {
		byte[] buffer = new byte[len];

		int n = 0;
		while (n < len) {
			int count = in.read(buffer, n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}

		return buffer;
	}
}

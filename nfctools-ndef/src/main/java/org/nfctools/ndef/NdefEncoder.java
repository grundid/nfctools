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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NdefEncoder {

	private NdefRecordEncoder ndefRecordEncoder;
	private NdefMessageEncoder ndefMessageEncoder;
	
	public NdefEncoder(NdefRecordEncoder ndefRecordEncoder, NdefMessageEncoder ndefMessageEncoder) {
		this.ndefRecordEncoder = ndefRecordEncoder;
		this.ndefMessageEncoder = ndefMessageEncoder;
	}

	public byte[] encode(Record record) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			encode(record, baos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return baos.toByteArray();
	}
	
	public void encode(Record record, OutputStream out) throws IOException {
		NdefRecord ndefRecord = ndefRecordEncoder.encode(record, this);
		ndefMessageEncoder.encode(ndefRecord, out);
	}

	public byte[] encode(Record... records) {
		return encode(Arrays.asList(records));
	}

	public byte[] encode(List<? extends Record> records) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			encode(records, baos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return baos.toByteArray();
	}

	public void encode(List<? extends Record> records, OutputStream out) throws IOException {
		List<NdefRecord> ndefRecords = new ArrayList<NdefRecord>();

		for(Record record : records) {
			ndefRecords.add(ndefRecordEncoder.encode(record, this));
		}
		
		ndefMessageEncoder.encode(ndefRecords, out);
	}

	
}

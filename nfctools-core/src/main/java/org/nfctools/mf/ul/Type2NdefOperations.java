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
package org.nfctools.mf.ul;

import java.io.IOException;
import java.util.List;

import org.nfctools.NfcException;
import org.nfctools.api.TagInfo;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.ndef.AbstractNdefOperations;
import org.nfctools.mf.tlv.NdefMessageTlv;
import org.nfctools.mf.tlv.TypeLengthValueReader;
import org.nfctools.mf.tlv.TypeLengthValueWriter;
import org.nfctools.ndef.Record;
import org.nfctools.tags.TagInputStream;
import org.nfctools.tags.TagOutputStream;

public class Type2NdefOperations extends AbstractNdefOperations {

	private MemoryLayout memoryLayout;
	private MfUlReaderWriter readerWriter;

	public Type2NdefOperations(MemoryLayout memoryLayout, MfUlReaderWriter readerWriter, TagInfo tagInfo,
			boolean formatted, boolean writable) {
		super(tagInfo, formatted, writable);
		this.memoryLayout = memoryLayout;
		this.readerWriter = readerWriter;
	}

	public MemoryLayout getMemoryLayout() {
		return memoryLayout;
	}

	@Override
	public int getMaxSize() {
		return memoryLayout.getMaxSize();
	}

	@Override
	public List<Record> readNdefMessage() {
		assertFormatted();
		if (lastReadRecords != null) {
			return lastReadRecords;
		}
		else {
			TypeLengthValueReader reader = new TypeLengthValueReader(new TagInputStream(memoryLayout, readerWriter));
			convertRecords(reader);
			return lastReadRecords;
		}
	}

	@Override
	public void writeNdefMessage(Record... records) {
		lastReadRecords = null;
		assertWritable();
		assertFormatted();
		byte[] bytes = convertNdefMessage(records);
		writeBufferOnTag(bytes);
	}

	@Override
	public void makeReadOnly() {
		assertWritable();
		assertFormatted();
		setLockBytes();
		writable = false;
	}

	@Override
	public void format(Record... records) {
		try {
			formatCapabilityBlock();
			writeNdefMessage(records);
		}
		catch (IOException e) {
			throw new NfcException(e);
		}
	}

	private byte[] convertNdefMessage(Record... records) {
		TagOutputStream out = new TagOutputStream(getMaxSize());
		TypeLengthValueWriter writer = new TypeLengthValueWriter(out);
		if (memoryLayout.hasDynamicLockBytes()) {
			writer.write(memoryLayout.createLockControlTlv());
		}
		writer.write(new NdefMessageTlv(convertRecordsToBytes(records)));
		writer.close();
		return out.getBuffer();
	}

	private void writeBufferOnTag(byte[] buffer) {
		assertWritable();
		assertFormatted();
		try {
			int offset = 0;
			for (int page = memoryLayout.getFirstDataPage(); page <= memoryLayout.getLastDataPage(); page++) {
				DataBlock block = new DataBlock(buffer, offset);
				readerWriter.writeBlock(page, block);
				offset += memoryLayout.getBytesPerPage();
			}
		}
		catch (IOException e) {
			throw new NfcException(e);
		}
	}

	private void formatCapabilityBlock() throws IOException {
		assertWritable();
		CapabilityBlock block = memoryLayout.createCapabilityBlock();
		readerWriter.writeBlock(memoryLayout.getCapabilityPage(), block);
		formatted = true;
	}

	private void setLockBytes() {
		try {
			for (LockPage lockPage : memoryLayout.getLockPages()) {
				MfBlock[] block = readerWriter.readBlock(lockPage.getPage(), 1);
				for (int lockByte : lockPage.getLockBytes()) {
					block[0].getData()[lockByte] = (byte)0xff;
				}
				readerWriter.writeBlock(lockPage.getPage(), block);
			}
			MfBlock[] readBlock = readerWriter.readBlock(memoryLayout.getCapabilityPage(), 1);
			CapabilityBlock capabilityBlock = new CapabilityBlock(readBlock[0].getData());
			capabilityBlock.setReadOnly();
			readerWriter.writeBlock(memoryLayout.getCapabilityPage(), capabilityBlock);
		}
		catch (IOException e) {
			throw new NfcException(e);
		}
	}
}

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
package org.nfctools.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.nfctools.api.ApduTag;
import org.nfctools.api.TagType;
import org.nfctools.mf.ul.MemoryMap;
import org.nfctools.scio.Command;
import org.nfctools.scio.Response;
import org.nfctools.spi.acs.Apdu;

public class InMemoryTag implements ApduTag {

	private MemoryMap memoryMap;

	public InMemoryTag(MemoryMap memoryMap) {
		this.memoryMap = memoryMap;
	}

	public MemoryMap getMemoryMap() {
		return memoryMap;
	}

	@Override
	public Response transmit(Command command) {
		if (command.getInstruction() == Apdu.INS_READ_BINARY) {
			return readBinary(command);
		}
		else if (command.getInstruction() == Apdu.INS_UPDATE_BINARY) {
			return writeBinary(command);
		}
		else if (command.getInstruction() == Apdu.INS_INTERNAL_AUTHENTICATE_ACS) {
			return new Response(0x90, 0, null);
		}
		else if (command.getInstruction() == Apdu.INS_EXTERNAL_AUTHENTICATE) {
			return new Response(0x90, 0, null);
		}
		throw new RuntimeException("Unknown command: " + command.getInstruction());
	}

	private Response writeBinary(Command command) {
		byte[] data = command.getData();
		int page = command.getP2();
		for (int offset = 0; offset < data.length; offset = offset + memoryMap.getBytesPerPage()) {
			memoryMap.setPage(page, data, offset);
			page++;
		}
		return new Response(0x90, 0, null);
	}

	private Response readBinary(Command command) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			int dataRead = 0;
			int page = command.getP2() & 0xFF;
			while (dataRead < command.getLength()) {
				byte[] data = memoryMap.getPage(page);
				out.write(data);
				dataRead += data.length;
				page++;
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new Response(0x90, 0x00, out.toByteArray());
	}

	@Override
	public TagType getTagType() {
		return TagType.IN_MEMORY;
	}
}

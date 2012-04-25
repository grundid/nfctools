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

package org.nfctools.mf.tlv;

import org.nfctools.tags.TagOutputStream;

public class TypeLengthValueWriter {

	private TagOutputStream out;

	public TypeLengthValueWriter(TagOutputStream out) {
		this.out = out;
	}

	public void write(LockControlTlv lockControlTlv) {
		out.write(TlvConstants.LOCK_CONTROL_TLV);
		writeData(lockControlTlv.toBytes());
	}

	public void write(MemoryControlTlv memoryControlTlv) {
		out.write(TlvConstants.MEMORY_CONTROL_TLV);
		writeData(memoryControlTlv.toBytes());
	}

	public void write(NdefMessageTlv ndefMessageTlv) {
		byte[] data = ndefMessageTlv.getNdefMessage();
		out.write(TlvConstants.NDEF_TLV);
		writeData(data);
	}

	public void writeNullTlv() {
		out.write(TlvConstants.NULL_TLV);
	}

	private void writeData(byte[] data) {
		if (data.length <= 0xFE) {
			out.write(data.length);
		}
		else if (data.length < 0xFFFE) {
			out.write(0xFF);
			out.write(data.length >>> 8);
			out.write(data.length & 0xFF);
		}
		else {
			throw new IllegalArgumentException("data too long");
		}

		out.write(data, 0, data.length);
	}

	public void close() {
		if (out.getRemainingSize() > 0) {
			out.write(TlvConstants.TERMINATOR_TLV);
			while (out.getRemainingSize() > 0)
				writeNullTlv();
		}
	}
}

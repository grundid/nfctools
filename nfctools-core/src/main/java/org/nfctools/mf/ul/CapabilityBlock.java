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

public class CapabilityBlock extends DataBlock {

	public CapabilityBlock(byte[] data) {
		super(data);
	}

	public CapabilityBlock(byte version, byte size, boolean readOnly) {
		super(new byte[4]);
		format(version, size, readOnly);
	}

	public void format(byte version, byte size, boolean readOnly) {
		data[0] = (byte)0xE1;
		setVersion(version);
		data[2] = size;
		if (readOnly)
			setReadOnly();
	}

	public byte getSize() {
		return data[2];
	}

	public byte getVersion() {
		return data[1];
	}

	public void setVersion(byte version) {
		data[1] = version;
	}

	public boolean isReadOnly() {
		return data[3] == 0x0f;
	}

	public void setReadOnly() {
		data[3] = 0x0f;
	}
}

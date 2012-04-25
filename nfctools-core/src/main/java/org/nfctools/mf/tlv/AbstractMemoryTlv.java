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

import org.nfctools.utils.NfcUtils;

public class AbstractMemoryTlv extends Tlv {

	protected int pageAddress;
	protected int byteOffset;
	protected int size;
	protected int bytesPerPage;

	public AbstractMemoryTlv() {
	}

	public AbstractMemoryTlv(byte[] bytes) {
		pageAddress = NfcUtils.getMostSignificantNibble(bytes[0]);
		byteOffset = NfcUtils.getLeastSignificantNibble(bytes[0]);
		size = bytes[1];
		bytesPerPage = NfcUtils.getLeastSignificantNibble(bytes[2]);
	}

	public byte[] toBytes() {
		byte[] bytes = new byte[3];
		bytes[0] = NfcUtils.encodeNibbles(pageAddress, byteOffset);
		bytes[1] = (byte)size;
		bytes[2] = NfcUtils.encodeNibbles(0, bytesPerPage);
		return bytes;
	}

	public int getPosition() {
		return (int)Math.round(pageAddress * (int)Math.pow(2, bytesPerPage) + byteOffset);
	}

	public int getPageAddress() {
		return pageAddress;
	}

	public void setPageAddress(int pageAddress) {
		this.pageAddress = pageAddress;
	}

	public int getByteOffset() {
		return byteOffset;
	}

	public void setByteOffset(int byteOffset) {
		this.byteOffset = byteOffset;
	}

	public int getBytesPerPage() {
		return bytesPerPage;
	}

	public void setBytesPerPage(int bytesPerPage) {
		this.bytesPerPage = bytesPerPage;
	}

	public int getSize() {
		if (size == 0)
			return 256;
		else
			return size;
	}

	public void setSize(int size) {
		if (size == 256)
			size = 0;
		else
			this.size = size;
	}

}

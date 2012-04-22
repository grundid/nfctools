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
package org.nfctools.ndef.mime;

import java.util.Arrays;

public class BinaryMimeRecord extends MimeRecord {

	private byte[] content;

	public BinaryMimeRecord(String contentType, byte[] content) {
		super(contentType);
		this.content = content;
	}

	public BinaryMimeRecord() {
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public byte[] getContentAsBytes() {
		return getContent();
	}

	@Override
	public String toString() {
		return "Content-Type: " + contentType + " Content: " + new String(content);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(content);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinaryMimeRecord other = (BinaryMimeRecord)obj;
		if (!Arrays.equals(content, other.content))
			return false;
		return true;
	}

	public boolean hasContent() {
		return content != null;
	}

}

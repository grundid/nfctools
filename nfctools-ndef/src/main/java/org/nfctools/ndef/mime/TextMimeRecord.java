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

import java.nio.charset.Charset;

import org.nfctools.ndef.NdefConstants;

public class TextMimeRecord extends MimeRecord {

	private String content;
	private Charset encoding = NdefConstants.DEFAULT_CHARSET;

	public TextMimeRecord(String contentType, String content) {
		super(contentType);
		this.content = content;
	}

	public TextMimeRecord(String contentType, byte[] content) {
		super(contentType);
		this.content = new String(content, encoding);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public byte[] getContentAsBytes() {
		return content.getBytes(encoding);
	}

	@Override
	public String toString() {
		return "Content-Type: " + contentType + "; Content: [" + getContent() + "]";
	}

}

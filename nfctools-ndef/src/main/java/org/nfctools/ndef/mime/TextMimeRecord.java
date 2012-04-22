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
	private Charset charset = NdefConstants.DEFAULT_CHARSET;

	public TextMimeRecord(String contentType, String content) {
		super(contentType);

		this.charset = extractContentTypeCharset();

		this.content = content;
	}

	public TextMimeRecord(String contentType, byte[] content) {
		super(contentType);

		this.charset = extractContentTypeCharset();

		this.content = new String(content, charset);
	}

	public Charset extractContentTypeCharset() {
		int index = contentType.indexOf(';');

		if (index != -1) {
			// check for charset=
			int charsetIndex = contentType.indexOf("charset=", index);
			if (charsetIndex != -1) {
				int charsetEndIndex = contentType.indexOf(';', charsetIndex + 8);

				if (charsetEndIndex == -1) {
					return Charset.forName(contentType.substring(charsetIndex + 8));
				}
				else {
					return Charset.forName(contentType.substring(charsetIndex + 8, charsetEndIndex).trim());
				}
			}
		}
		return NdefConstants.DEFAULT_CHARSET;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public byte[] getContentAsBytes() {
		return content.getBytes(charset);
	}

	@Override
	public String toString() {
		return "Content-Type: " + contentType + "; Charset: " + charset + "; Content: [" + getContent() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((charset == null) ? 0 : charset.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
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
		TextMimeRecord other = (TextMimeRecord)obj;
		if (charset == null) {
			if (other.charset != null)
				return false;
		}
		else if (!charset.equals(other.charset))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		}
		else if (!content.equals(other.content))
			return false;
		return true;
	}

}

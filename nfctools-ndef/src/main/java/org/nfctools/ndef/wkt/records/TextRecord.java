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
package org.nfctools.ndef.wkt.records;

import java.nio.charset.Charset;
import java.util.Locale;

public class TextRecord extends WellKnownRecord {

	public static final byte LANGUAGE_CODE_MASK = 0x1F;

	public static final Charset UTF8 = Charset.forName("UTF-8");
	public static final Charset UTF16 = Charset.forName("UTF-16BE");

	private String text;
	private Charset encoding;
	private Locale locale;

	public TextRecord(String key, String text) {
		this(text, UTF8, Locale.getDefault());
		setKey(key);
	}

	public TextRecord(String text) {
		this(text, UTF8, Locale.getDefault());
	}

	public TextRecord(String text, Locale locale) {
		this(text, UTF8, locale);
	}

	public TextRecord(String text, Charset encoding, Locale locale) {
		this.encoding = encoding;
		this.text = text;
		this.locale = locale;
		if (!encoding.equals(UTF8) && !encoding.equals(UTF16))
			throw new IllegalArgumentException("unsupported encoding. only utf8 and utf16 are allowed.");
	}

	public TextRecord() {
	}

	public String getText() {
		return text;
	}

	public Locale getLocale() {
		return locale;
	}

	public Charset getEncoding() {
		return encoding;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Title: [");

		if (hasKey())
			sb.append("Key/Id: ").append(getKey()).append(", ");

		sb.append("Text: ").append(text).append(", ");
		sb.append("Locale: " + locale.getLanguage()).append(
				locale.getCountry() == null || locale.getCountry().length() == 0 ? "" : ("-" + locale.getCountry()));

		sb.append("]");
		return sb.toString();
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setEncoding(Charset encoding) {
		if (!encoding.equals(UTF8) && !encoding.equals(UTF16))
			throw new IllegalArgumentException("unsupported encoding. only utf8 and utf16 are allowed.");

		this.encoding = encoding;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public boolean hasText() {
		return text != null;
	}

	public boolean hasLocale() {
		return locale != null;
	}

	public boolean hasEncoding() {
		return encoding != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		TextRecord other = (TextRecord)obj;
		if (encoding == null) {
			if (other.encoding != null)
				return false;
		}
		else if (!encoding.equals(other.encoding))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		}
		else if (!locale.equals(other.locale))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		}
		else if (!text.equals(other.text))
			return false;
		return true;
	}
}

/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
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

import org.nfctools.ndef.Record;

public class TextRecord extends Record {

	public static final byte[] TYPE = { 'T' };
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
}

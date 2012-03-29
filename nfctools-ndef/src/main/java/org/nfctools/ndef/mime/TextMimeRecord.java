package org.nfctools.ndef.mime;

import java.io.UnsupportedEncodingException;

public class TextMimeRecord extends MimeRecord {

	private static final String DEFAULT_ENCODING = "utf8";
	private String content;

	public TextMimeRecord(String contentType, String content) {
		super(contentType);
		this.content = content;
	}

	public TextMimeRecord(String contentType, byte[] content) {
		super(contentType);
		try {
			this.content = new String(content, DEFAULT_ENCODING);
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public byte[] getContentAsBytes() {
		try {
			return content.getBytes(DEFAULT_ENCODING);
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Content-Type: " + contentType + "; Content: [" + getContent() + "]";
	}

}

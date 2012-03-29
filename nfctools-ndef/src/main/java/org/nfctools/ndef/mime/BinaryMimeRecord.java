package org.nfctools.ndef.mime;

public class BinaryMimeRecord extends MimeRecord {

	private byte[] content;

	public BinaryMimeRecord(String contentType, byte[] content) {
		super(contentType);
		this.content = content;
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
}

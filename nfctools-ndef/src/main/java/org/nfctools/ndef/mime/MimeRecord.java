package org.nfctools.ndef.mime;

import org.nfctools.ndef.Record;

public abstract class MimeRecord extends Record {

	protected String contentType;

	protected MimeRecord(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public abstract byte[] getContentAsBytes();
}

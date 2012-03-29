package org.nfctools.ndef.ext;

import org.nfctools.ndef.Record;

public class ExternalType extends Record {

	private String namespace;
	private String content;

	public ExternalType(String namespace, String content) {
		this.namespace = namespace;
		this.content = content;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Namespace: [" + namespace + "] Content: [" + content + "]";
	}
}

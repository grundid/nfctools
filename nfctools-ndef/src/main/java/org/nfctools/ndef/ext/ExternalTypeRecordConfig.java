package org.nfctools.ndef.ext;

import org.nfctools.ndef.Record;
import org.nfctools.ndef.RecordType;

public class ExternalTypeRecordConfig {

	private String namespace;
	private Class<? extends Record> recordClass;
	private ExternalTypeContentEncoder contentEncoder;
	private ExternalTypeContentDecoder contentDecoder;
	
	public ExternalTypeRecordConfig(String namespace, Class<? extends Record> recordClass, ExternalTypeContentEncoder contentEncoder, ExternalTypeContentDecoder contentDecoder) {
		this.namespace = namespace;
		this.recordClass = recordClass;
		this.contentEncoder = contentEncoder;
		this.contentDecoder = contentDecoder;
	}
	public ExternalTypeRecordConfig(RecordType recordType,
			Class<? extends ExternalTypeRecord> recordClass2,
			ExternalTypeContentEncoder payloadEncoder,
			ExternalTypeContentDecoder payloadDecoder) {
		// TODO Auto-generated constructor stub
	}
	public String getNamespace() {
		return namespace;
	}
	public Class<? extends Record> getRecordClass() {
		return recordClass;
	}
	public ExternalTypeContentEncoder getContentEncoder() {
		return contentEncoder;
	}
	public ExternalTypeContentDecoder getContentDecoder() {
		return contentDecoder;
	}

	
}

package org.nfctools.ndef.wkt;

import org.nfctools.ndef.Record;
import org.nfctools.ndef.RecordType;

public class WellKnownRecordConfig {

	private RecordType recordType;
	private Class<? extends Record> recordClass;
	private WellKnownRecordPayloadEncoder payloadEncoder;
	private WellKnownRecordPayloadDecoder payloadDecoder;

	public WellKnownRecordConfig(RecordType recordType, Class<? extends Record> recordClass,
			WellKnownRecordPayloadEncoder payloadEncoder, WellKnownRecordPayloadDecoder payloadDecoder) {
		this.recordType = recordType;
		this.recordClass = recordClass;
		this.payloadEncoder = payloadEncoder;
		this.payloadDecoder = payloadDecoder;
	}

	public RecordType getRecordType() {
		return recordType;
	}

	public Class<? extends Record> getRecordClass() {
		return recordClass;
	}

	public WellKnownRecordPayloadEncoder getPayloadEncoder() {
		return payloadEncoder;
	}

	public WellKnownRecordPayloadDecoder getPayloadDecoder() {
		return payloadDecoder;
	}
}

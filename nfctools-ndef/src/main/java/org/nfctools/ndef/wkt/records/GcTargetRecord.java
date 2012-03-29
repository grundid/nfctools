package org.nfctools.ndef.wkt.records;

import org.nfctools.ndef.Record;

public class GcTargetRecord extends Record {

	public static final byte[] TYPE = { 't' };

	private Record targetIdentifier;

	public GcTargetRecord(Record targetIdentifier) {
		setTargetIdentifier(targetIdentifier);
	}

	public void setTargetIdentifier(Record targetIdentifier) {
		if ((targetIdentifier instanceof UriRecord) || (targetIdentifier instanceof TextRecord))
			this.targetIdentifier = targetIdentifier;
		else
			throw new IllegalArgumentException(targetIdentifier.getClass().getName()
					+ " not supported as target identifier");
	}

	public Record getTargetIdentifier() {
		return targetIdentifier;
	}

	@Override
	public String toString() {
		return "Target: [" + targetIdentifier + "]";
	}
}

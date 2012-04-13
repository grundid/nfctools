package org.nfctools.ndef.wkt;

import java.util.HashMap;
import java.util.Map;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class WellKnownRecordEncoder implements RecordEncoder {

	private Map<Class<?>, WellKnownRecordConfig> knownRecordTypes = new HashMap<Class<?>, WellKnownRecordConfig>();

	@Override
	public boolean canEncode(Record record) {
		return knownRecordTypes.containsKey(record.getClass());
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		WellKnownRecordConfig config = knownRecordTypes.get(record.getClass());
		byte[] payload = config.getPayloadEncoder().encodePayload((WellKnownRecord)record, messageEncoder);
		byte[] type = config.getRecordType().getType();
		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, type, record.getId(), payload);
	}

	public void addRecordConfig(WellKnownRecordConfig config) {
		knownRecordTypes.put(config.getRecordClass(), config);
	}
}

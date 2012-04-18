package org.nfctools.ndef.wkt;

import java.util.HashMap;
import java.util.Map;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.RecordType;
import org.nfctools.ndef.wkt.decoder.RecordDecoder;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class WellKnownRecordDecoder implements RecordDecoder<WellKnownRecord> {

	private Map<RecordType, WellKnownRecordConfig> recordDecoders = new HashMap<RecordType, WellKnownRecordConfig>();

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		if(NdefConstants.TNF_WELL_KNOWN == ndefRecord.getTnf()) {
			return recordDecoders.containsKey(new RecordType(ndefRecord.getType()));
		}
		return false;
	}

	@Override
	public WellKnownRecord decodeRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		WellKnownRecordConfig config = recordDecoders.get(new RecordType(ndefRecord.getType()));
		if (config != null) {
			WellKnownRecordPayloadDecoder payloadDecoder = config.getPayloadDecoder();
			WellKnownRecord record = payloadDecoder.decodePayload(ndefRecord.getPayload(), messageDecoder);
			record.setId(ndefRecord.getId());
			return record;
		}
		else
			throw new IllegalArgumentException("Unsupported Well Known NDEF Type [" + new String(ndefRecord.getType())
					+ "]");
	}

	public void addRecordConfig(WellKnownRecordConfig config) {
		recordDecoders.put(config.getRecordType(), config);
	}
}

package org.nfctools.ndef.wkt.encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.GenericControlRecord;

public class GenericControlRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof GenericControlRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {

		GenericControlRecord myRecord = (GenericControlRecord)record;

		byte[] subPayload = createSubPayload(messageEncoder, myRecord);

		byte[] payload = new byte[subPayload.length + 1];
		payload[0] = myRecord.getConfigurationByte();
		System.arraycopy(subPayload, 0, payload, 1, subPayload.length);

		return new NdefRecord(NdefConstants.TNF_WELL_KNOWN, GenericControlRecord.TYPE, record.getId(), payload);
	}

	private byte[] createSubPayload(NdefMessageEncoder messageEncoder, GenericControlRecord myRecord) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(messageEncoder.encodeSingle(myRecord.getTarget()));

			if (myRecord.getAction() != null)
				baos.write(messageEncoder.encodeSingle(myRecord.getAction()));
			if (myRecord.getData() != null)
				baos.write(messageEncoder.encodeSingle(myRecord.getData()));

			return baos.toByteArray();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}

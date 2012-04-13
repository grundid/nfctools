package org.nfctools.ndef.wkt.encoder;

import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public interface PayloadEncoder {

	/**
	 * @param record
	 * @param messageEncoder
	 * @return only the encoded payload of the record
	 */
	byte[] encodeRecordPayload(WellKnownRecord record, NdefMessageEncoder messageEncoder);

}

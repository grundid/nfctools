package org.nfctools.ndef.wkt;

import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public interface WellKnownRecordPayloadEncoder {

	byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder);
}

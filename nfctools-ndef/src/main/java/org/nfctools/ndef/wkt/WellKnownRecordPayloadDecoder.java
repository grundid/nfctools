package org.nfctools.ndef.wkt;

import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public interface WellKnownRecordPayloadDecoder {

	WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder);
}

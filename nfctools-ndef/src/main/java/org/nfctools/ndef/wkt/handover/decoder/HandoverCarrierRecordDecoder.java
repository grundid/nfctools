/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nfctools.ndef.wkt.handover.decoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.ext.UnsupportedExternalTypeRecord;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.handover.records.HandoverCarrierRecord;
import org.nfctools.ndef.wkt.handover.records.HandoverCarrierRecord.CarrierTypeFormat;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class HandoverCarrierRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {

		HandoverCarrierRecord handoverCarrierRecord = new HandoverCarrierRecord();

		CarrierTypeFormat carrierTypeFormat = CarrierTypeFormat.toCarrierTypeFormat((byte)(payload[0] & 0x7));

		handoverCarrierRecord.setCarrierTypeFormat(carrierTypeFormat);

		int carrierTypeLength = (int)(payload[1] & 0xFF);
		byte[] carrierType = new byte[carrierTypeLength];
		System.arraycopy(payload, 2, carrierType, 0, carrierTypeLength);

		switch (carrierTypeFormat) {
			case WellKnown: {
				// NFC Forum well-known type [NFC RTD]

				Record record = messageDecoder.decodeToRecord(carrierType);

				if (record instanceof WellKnownRecord) {
					handoverCarrierRecord.setCarrierType(record);
				}
				else {
					throw new IllegalArgumentException("Expected well-known type carrier type");
				}

				break;
			}
			case Media: {

				// Media-type as defined in RFC 2046 [RFC 2046]
				handoverCarrierRecord.setCarrierType(new String(carrierType, NdefConstants.DEFAULT_CHARSET));

				break;
			}
			case AbsoluteURI: {
				// Absolute URI as defined in RFC 3986 [RFC 3986]
				handoverCarrierRecord.setCarrierType(new String(carrierType, NdefConstants.DEFAULT_CHARSET));

				break;
			}
			case External: {
				// NFC Forum external type [NFC RTD]

				Record record = messageDecoder.decodeToRecord(carrierType);

				if (record instanceof UnsupportedExternalTypeRecord) {
					handoverCarrierRecord.setCarrierType(record);
				}
				else {
					throw new IllegalArgumentException("Expected external type carrier type");
				}
			}
			default: {
				throw new RuntimeException();
			}

		}

		// The number of CARRIER_DATA octets is equal to the NDEF record PAYLOAD_LENGTH minus the CARRIER_TYPE_LENGTH minus 2.		
		int carrierDataLength = payload.length - 2 - carrierTypeLength;

		byte[] carrierData;
		if (carrierDataLength > 0) {
			carrierData = new byte[carrierDataLength];
			System.arraycopy(payload, 2 + carrierTypeLength, carrierData, 0, carrierDataLength);
		}
		else {
			carrierData = NdefConstants.EMPTY_BYTE_ARRAY;
		}
		handoverCarrierRecord.setCarrierData(carrierData);

		return handoverCarrierRecord;
	}

}

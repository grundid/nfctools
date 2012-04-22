/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
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
package org.nfctools.ndef.wkt.decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.RecordUtils;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.records.SignatureRecord;
import org.nfctools.ndef.wkt.records.SignatureRecord.CertificateFormat;
import org.nfctools.ndef.wkt.records.SignatureRecord.SignatureType;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class SignatureRecordDecoder implements WellKnownRecordPayloadDecoder {

	@Override
	public WellKnownRecord decodePayload(byte[] payload, NdefMessageDecoder messageDecoder) {
		SignatureRecord signatureRecord = new SignatureRecord();
		
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(payload);

			int version = bais.read();
			
			signatureRecord.setVersion((byte)version);
			
			int header = bais.read();
			boolean signatureUriPresent = (header & 0x80) != 0;
			SignatureType type = SignatureType.toSignatureType((header & 0x7F));
			
			signatureRecord.setSignatureType(type);
			
			if(signatureUriPresent || type != SignatureType.NOT_PRESENT) {
				
				int size = RecordUtils.readUnsignedShort(bais);

				if(size > 0) {
					byte[] signatureOrUri = RecordUtils.readByteArray(bais, size);
	
					if(signatureUriPresent) {
						signatureRecord.setSignatureUri(new String(signatureOrUri, NdefConstants.UTF_8_CHARSET));
					} else {
						signatureRecord.setSignature(signatureOrUri);
					}
				}
				
				int certificateHeader = bais.read();
				
				signatureRecord.setCertificateFormat(CertificateFormat.toCertificateFormat((certificateHeader >> 4) & 0x7));
				
				int numberOfCertificates = certificateHeader & 0xF;

				for(int i = 0; i < numberOfCertificates; i++) {
					int certificateSize = RecordUtils.readUnsignedShort(bais);

					byte[] certificate = RecordUtils.readByteArray(bais, certificateSize);
					
					signatureRecord.add(certificate);
				}
				
				if((certificateHeader & 0x80) != 0) { // has certificate uri
					int certificateUriSize = RecordUtils.readUnsignedShort(bais);

					byte[] certificateUri = RecordUtils.readByteArray(bais, certificateUriSize);
					
					signatureRecord.setCertificateUri(new String(certificateUri, NdefConstants.UTF_8_CHARSET));
				}
				
			} else {
				// start marker
			}
		} catch(IOException e) {
			throw new IllegalArgumentException("Unable to decode", e);
		}
		
		return signatureRecord;
	}

		
}

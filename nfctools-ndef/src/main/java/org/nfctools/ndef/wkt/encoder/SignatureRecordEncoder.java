package org.nfctools.ndef.wkt.encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.records.SignatureRecord;
import org.nfctools.ndef.wkt.records.SignatureRecord.CertificateFormat;
import org.nfctools.ndef.wkt.records.WellKnownRecord;

public class SignatureRecordEncoder implements WellKnownRecordPayloadEncoder {

	@Override
	public byte[] encodePayload(WellKnownRecord wellKnownRecord, NdefMessageEncoder messageEncoder) {
		SignatureRecord myRecord = (SignatureRecord )wellKnownRecord;
		return createPayload(messageEncoder, myRecord);
	}

	private byte[] createPayload(NdefMessageEncoder messageEncoder, SignatureRecord signatureRecord) {
		if(signatureRecord.isStartMarker()) {
			return new byte[]{0x01, 0x00};// version 1 and type 0
		} else {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				baos.write(signatureRecord.getVersion());

				if(!signatureRecord.hasSignatureType()) {
					throw new IllegalArgumentException("Expected signature type");
				}
				baos.write((1 << 7) | (signatureRecord.getSignatureType().getValue() & 0x7F));

				if(signatureRecord.hasSignature() && signatureRecord.hasSignatureUri()) {
					throw new IllegalArgumentException("Expected signature or signature uri, not both");
				} else if(!signatureRecord.hasSignature() && !signatureRecord.hasSignatureUri()) {
					throw new IllegalArgumentException("Expected signature or signature uri");
				}

				byte[] signatureOrUri;
				if(signatureRecord.hasSignature()) {
					signatureOrUri = signatureRecord.getSignature();

					if(signatureOrUri.length > 65535) {
						throw new IllegalArgumentException("Expected signature size " + signatureOrUri.length + " <= 65535");
					}
				} else {
					signatureOrUri = signatureRecord.getSignatureUri().getBytes(NdefConstants.UTF_8_CHARSET);

					if(signatureOrUri.length > 65535) {
						throw new IllegalArgumentException("Expected signature uri byte size " + signatureOrUri.length + " <= 65535");
					}
				}

				baos.write((signatureOrUri.length >> 8) & 0xFF);
				baos.write(signatureOrUri.length & 0xFF);

				baos.write(signatureOrUri);

				if(!signatureRecord.hasCertificateFormat()) {
					throw new IllegalArgumentException("Expected certificate format");
				}

				List<byte[]> certificates = signatureRecord.getCertificates();
				if(certificates.size() > 16) {
					throw new IllegalArgumentException("Expected number of certificates " + certificates.size() + " <= 15");
				}

				CertificateFormat certificateFormat = signatureRecord.getCertificateFormat();
				baos.write(((signatureRecord.hasCertificateUri() ? 1 : 0) << 7) | (certificateFormat.getValue() << 4) | (certificates.size() & 0xF));

				for(int i = 0; i < certificates.size(); i++) {
					byte[] certificate = certificates.get(i);

					if(certificate.length > 65535) {
						throw new IllegalArgumentException("Expected certificate " + i + " size " + certificate.length + " <= 65535");
					}

					baos.write((certificate.length >> 8) & 0xFF);
					baos.write(certificate.length & 0xFF);
					baos.write(certificate);
				}

				if(signatureRecord.hasCertificateUri()) {

					byte[] certificateUri = signatureRecord.getCertificateUri().getBytes(NdefConstants.UTF_8_CHARSET);

					if(certificateUri.length > 65535) {
						throw new IllegalArgumentException("Expected certificate uri byte size " + certificateUri.length + " <= 65535");
					}

					baos.write((certificateUri.length >> 8) & 0xFF);
					baos.write(certificateUri.length & 0xFF);
					baos.write(certificateUri);
				}
				return baos.toByteArray();
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}

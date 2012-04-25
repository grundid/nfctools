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
package org.nfctools.ndef.wkt.records;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SignatureRecord extends WellKnownRecord {
	
	public enum SignatureType {
		
		NOT_PRESENT((byte)0x00), // No signature present
		RSASSA_PSS_SHA_1((byte)0x01), // PKCS_1
		RSASSA_PKCS1_v1_5_WITH_SHA_1((byte)0x02), // PKCS_1
		DSA((byte)0x03),
		ECDSA((byte)0x04);
		
		private SignatureType(byte value) {
			this.value = value;
		}

		private byte value;
		
		public byte getValue() {
			return value;
		}

		public static SignatureType toSignatureType(int i) {
			for(SignatureType type : values()) {
				if(type.value == i) {
					return type;
				}
			}
			
			throw new IllegalArgumentException("Unexpected signature type " + i);
		}
		
	}
	
	public enum CertificateFormat {
		X_509((byte)0x00),
		X9_68((byte)0x01);
		
		private CertificateFormat(byte value) {
			this.value = value;
		}

		private byte value;
		
		public byte getValue() {
			return value;
		}

		public static CertificateFormat toCertificateFormat(int i) {
			for(CertificateFormat type : values()) {
				if(type.value == i) {
					return type;
				}
			}
			
			throw new IllegalArgumentException("Unexpected certificate format " + i);
		}
		
	}
	
	private byte version = 0x01;
	
	private SignatureType signatureType;

	private byte[] signature;
	
	private String signatureUri;
	
	private CertificateFormat certificateFormat;
	
	private String certificateUri;
	
	private List<byte[]> certificates = new ArrayList<byte[]>();

	public SignatureRecord() {
	}
	
	public SignatureRecord(SignatureType signatureType) {
		this.signatureType = signatureType;
	}
	
	public SignatureRecord(SignatureType signatureType, byte[] signature) {
		this(signatureType);
		this.signature = signature;
	}

	public SignatureRecord(SignatureType signatureType, String signatureUri) {
		this(signatureType);
		this.signatureUri = signatureUri;
	}

	public SignatureRecord(SignatureType signatureType, byte[] signature, CertificateFormat certificateFormat) {
		this(signatureType, signature);
		this.certificateFormat = certificateFormat;
	}

	public SignatureRecord(SignatureType signatureType, String signatureUri, CertificateFormat certificateFormat) {
		this(signatureType, signatureUri);
		this.certificateFormat = certificateFormat;
	}
	
	public SignatureRecord(SignatureType signatureType, byte[] signature, CertificateFormat certificateFormat, String certificateUri) {
		this(signatureType, signature, certificateFormat);
		this.signature = signature;
	}

	public SignatureRecord(SignatureType signatureType, String signatureUri, CertificateFormat certificateFormat, String certificateUri) {
		this(signatureType, signatureUri, certificateFormat);
		this.signatureUri = signatureUri;
	}


	
	public boolean isStartMarker() {
		return signatureType == SignatureType.NOT_PRESENT && signature == null && signatureUri == null;
	}
	
	public boolean hasCertificateUri() {
		return certificateUri != null;
	}
	
	public boolean hasSignature() {
		return signature != null;
	}
	
	public boolean hasSignatureUri() {
		return signatureUri != null;
	}
	
	public SignatureType getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(SignatureType signatureType) {
		this.signatureType = signatureType;
	}

	public CertificateFormat getCertificateFormat() {
		return certificateFormat;
	}

	public void setCertificateFormat(CertificateFormat certificateFormat) {
		this.certificateFormat = certificateFormat;
	}

	public List<byte[]> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<byte[]> certificates) {
		this.certificates = certificates;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public String getSignatureUri() {
		return signatureUri;
	}

	public void setSignatureUri(String signatureUri) {
		this.signatureUri = signatureUri;
	}

	public String getCertificateUri() {
		return certificateUri;
	}

	public void setCertificateUri(String certificateUri) {
		this.certificateUri = certificateUri;
	}

	public boolean hasSignatureType() {
		return signatureType != null;
	}

	public boolean hasCertificateFormat() {
		return certificateFormat != null;
	}

	public void add(byte[] certificate) {
		this.certificates.add(certificate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((certificateFormat == null) ? 0 : certificateFormat
						.hashCode());
		result = prime * result
				+ ((certificateUri == null) ? 0 : certificateUri.hashCode());
		result = prime * result
				+ ((certificates == null) ? 0 : certificatesHash());
		result = prime * result + Arrays.hashCode(signature);
		result = prime * result
				+ ((signatureType == null) ? 0 : signatureType.hashCode());
		result = prime * result
				+ ((signatureUri == null) ? 0 : signatureUri.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SignatureRecord other = (SignatureRecord) obj;
		if (certificateFormat != other.certificateFormat)
			return false;
		if (certificateUri == null) {
			if (other.certificateUri != null)
				return false;
		} else if (!certificateUri.equals(other.certificateUri))
			return false;
		if (!Arrays.equals(signature, other.signature))
			return false;
		if (signatureType != other.signatureType)
			return false;
		if (signatureUri == null) {
			if (other.signatureUri != null)
				return false;
		} else if (!signatureUri.equals(other.signatureUri))
			return false;
		if (version != other.version)
			return false;
		
		return certificatesEquals(other);
	}
	
	private int certificatesHash() {
		int hash;
		
		if(certificates != null) {
			hash = certificates.size();
			
			for(byte[] certificate : certificates) {
				hash += Arrays.hashCode(certificate);
			}
		} else {
			hash = 0;
		}
		return hash;
	}

	private boolean certificatesEquals(SignatureRecord other) {
		if (certificates == null) {
			if (other.certificates != null)
				return false;
		} else {
			if (other.certificates == null) {
				return false;
			}
			if(other.certificates.size() != certificates.size()) {
				return false;
			}
			
			for(int i = 0; i < certificates.size(); i++) {
				byte[] otherCertificate = other.certificates.get(i);
				byte[] thisCertificate = certificates.get(i);
				
				if(!Arrays.equals(otherCertificate, thisCertificate)) {
					return false;
				}
			}
		}
		return true;
	}
	
	
}

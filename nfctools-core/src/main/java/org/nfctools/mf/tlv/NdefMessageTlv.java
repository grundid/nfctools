package org.nfctools.mf.tlv;

public class NdefMessageTlv extends Tlv {

	private byte[] ndefMessage;

	public NdefMessageTlv(byte[] ndefMessage) {
		this.ndefMessage = ndefMessage;
	}

	public byte[] getNdefMessage() {
		return ndefMessage;
	}

	public void setNdefMessage(byte[] ndefMessage) {
		this.ndefMessage = ndefMessage;
	}
}

package org.nfctools.api;

public class ConnectionSetup {

	public byte[] mifareParams;
	public byte[] felicaParams;
	public byte[] nfcId3t;
	public byte[] generalBytes;

	public ConnectionSetup(byte[] mifareParams, byte[] felicaParams, byte[] nfcId3t, byte[] generalBytes) {
		this.mifareParams = mifareParams;
		this.felicaParams = felicaParams;
		this.nfcId3t = nfcId3t;
		this.generalBytes = generalBytes;
	}
}

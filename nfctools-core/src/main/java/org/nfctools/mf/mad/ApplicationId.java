package org.nfctools.mf.mad;

public class ApplicationId {

	private byte[] aid;

	public ApplicationId(byte[] aid) {
		this.aid = aid;
	}

	public ApplicationId(byte applicationCode, byte functionClusterCode) {
		aid = new byte[2];
		aid[0] = applicationCode;
		aid[1] = functionClusterCode;
	}

	public byte[] getAid() {
		return aid;
	}
}

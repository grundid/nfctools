package org.nfctools.spi.tama.request;

public class SelectReq {

	private int targetId;

	public SelectReq(int targetId) {
		this.targetId = targetId;
	}

	public int getTargetId() {
		return targetId;
	}

}

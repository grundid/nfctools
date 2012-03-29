package org.nfctools.spi.tama.request;

public class ReleaseReq {

	private int targetId;

	public ReleaseReq(int targetId) {
		this.targetId = targetId;
	}

	public int getTargetId() {
		return targetId;
	}

}

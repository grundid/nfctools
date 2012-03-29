package org.nfctools.spi.tama.request;

public class DeselectReq {

	private int targetId;

	public DeselectReq(int targetId) {
		this.targetId = targetId;
	}

	public int getTargetId() {
		return targetId;
	}

}

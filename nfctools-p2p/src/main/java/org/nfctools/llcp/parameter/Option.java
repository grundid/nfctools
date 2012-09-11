package org.nfctools.llcp.parameter;

public class Option {

	private int linkServiceClass;

	public Option(int linkServiceClass) {
		this.linkServiceClass = linkServiceClass;
	}

	public int getLinkServiceClass() {
		return linkServiceClass;
	}

}

package org.nfctools.llcp.parameter;

public class Version {

	private byte major;
	private byte minor;

	public Version(int major, int minor) {
		this.major = (byte)major;
		this.minor = (byte)minor;
	}

	public byte getMajor() {
		return major;
	}

	public byte getMinor() {
		return minor;
	}

}

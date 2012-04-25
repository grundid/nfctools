package org.nfctools.mf.ul;

public class CapabilityBlock extends DataBlock {

	public CapabilityBlock(byte[] data) {
		super(data);
	}

	public CapabilityBlock(byte version, byte size, boolean readOnly) {
		super(new byte[4]);
		format(version, size, readOnly);
	}

	public void format(byte version, byte size, boolean readOnly) {
		data[0] = (byte)0xE1;
		setVersion(version);
		data[2] = size;
		if (readOnly)
			setReadOnly();
	}

	public byte getSize() {
		return data[2];
	}

	public byte getVersion() {
		return data[1];
	}

	public void setVersion(byte version) {
		data[1] = version;
	}

	public boolean isReadOnly() {
		return data[3] == 0x0f;
	}

	public void setReadOnly() {
		data[3] = 0x0f;
	}
}

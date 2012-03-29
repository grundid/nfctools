package org.nfctools.spi.tama.request;

public class RfCommunicationReq {

	private int configItem;
	private byte[] configData;

	public RfCommunicationReq(int configItem, byte[] configData) {
		this.configItem = configItem;
		this.configData = configData;
	}

	public int getConfigItem() {
		return configItem;
	}

	public byte[] getConfigData() {
		return configData;
	}

}

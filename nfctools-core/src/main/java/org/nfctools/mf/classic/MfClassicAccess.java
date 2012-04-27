package org.nfctools.mf.classic;


public class MfClassicAccess {

	private KeyValue keyValue;
	private int sector;
	private int block;
	private int blocksToRead = 1;

	public MfClassicAccess(KeyValue keyValue, int sector, int block, int blocksToRead) {
		this.keyValue = keyValue;
		this.sector = sector;
		this.block = block;
		this.blocksToRead = blocksToRead;
	}

	public MfClassicAccess(KeyValue keyValue, int sector, int block) {
		this.keyValue = keyValue;
		this.sector = sector;
		this.block = block;
	}

	public KeyValue getKeyValue() {
		return keyValue;
	}

	public int getSector() {
		return sector;
	}

	public int getBlock() {
		return block;
	}

	public int getBlocksToRead() {
		return blocksToRead;
	}

}

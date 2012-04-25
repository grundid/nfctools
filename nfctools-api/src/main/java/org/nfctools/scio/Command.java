package org.nfctools.scio;

public class Command {

	private int instruction;
	private int p1;
	private int p2;
	private int length;
	private byte[] data;

	public Command(int instruction, int p1, int p2, byte[] data) {
		this.instruction = instruction;
		this.p1 = p1;
		this.p2 = p2;
		this.length = data.length;
		this.data = data;
	}

	public Command(int instruction, int p1, int p2, int length) {
		this.instruction = instruction;
		this.p1 = p1;
		this.p2 = p2;
		this.length = length;
	}

	public int getInstruction() {
		return instruction;
	}

	public int getP1() {
		return p1;
	}

	public int getP2() {
		return p2;
	}

	public int getLength() {
		return length;
	}

	public byte[] getData() {
		return data;
	}

	public boolean hasData() {
		return data != null;
	}
}

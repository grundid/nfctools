/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nfctools.scio;

public class Command {

	private int instruction;
	private int p1;
	private int p2;
	private int length;
	private int offset;
	private byte[] data;
	private boolean dataOnly;

	public Command(byte[] data, int offset, int length) {
		this.data = data;
		this.offset = offset;
		this.length = length;
		dataOnly = true;
	}

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

	public boolean isDataOnly() {
		return dataOnly;
	}

	public int getOffset() {
		return offset;
	}

}

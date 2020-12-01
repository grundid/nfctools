/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 * Copyright 2020 https://github.com/tontg
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

public class Response {

	private int sw1;
	private int sw2;
	private byte[] data;

	public Response(int sw1, int sw2, byte[] data) {
		this.sw1 = sw1;
		this.sw2 = sw2;
		this.data = data;
	}

	public int getSw1() {
		return sw1;
	}

	public int getSw2() {
		return sw2;
	}

	public byte[] getData() {
		return data;
	}

        /**
         * SW1 & SW2 in hexadecimal format
         *
         * @see
         * https://www.eftlab.com/knowledge-base/complete-list-of-apdu-responses/
         */
        @Override
        public String toString() {
            return "SW1: 0x" + Integer.toHexString(sw1) + " SW2: 0x" + Integer.toHexString(sw2);
        }

	public boolean isSuccess() {
		return sw1 == 0x90 && sw2 == 0x00;
	}
}

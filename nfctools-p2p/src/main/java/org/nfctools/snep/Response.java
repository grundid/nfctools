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
package org.nfctools.snep;

public enum Response {

	CONTINUE(0x80), SUCCESS(0x81), NOT_FOUND(0xC0), EXCESS_DATA(0xC1), BAD_REQUEST(0xC2), NOT_IMPLEMENTED(0xE0), UNSUPPORTED_VERSION(
			0xE1), REJECT(0xFF);

	byte code;

	private Response(int code) {
		this.code = (byte)code;
	}

	public byte getCode() {
		return code;
	}

}

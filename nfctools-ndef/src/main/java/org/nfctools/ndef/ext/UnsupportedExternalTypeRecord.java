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
package org.nfctools.ndef.ext;


public class UnsupportedExternalTypeRecord extends ExternalTypeRecord {

	protected byte[] data;
	protected String domain;
	protected String type;

	public UnsupportedExternalTypeRecord(String domain, String type, byte[] data) {
		this.domain = domain;
		this.type = type;
		this.data = data;
	}

	public UnsupportedExternalTypeRecord() {
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean hasDomain() {
		return domain != null;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Domain: [" + domain + "] Type: [" + type + "] Data: [" + (data != null ? data.length : 0) + "]";
	}

	public boolean hasData() {
		return data != null;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean hasType() {
		return type != null;
	}

}

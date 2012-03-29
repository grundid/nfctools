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
package org.nfctools.spi.tama.response;

public class GetFirmwareVersionResp {

	private int ic;
	private int version;
	private int revision;
	private int support;

	public GetFirmwareVersionResp(int version, int revision) {
		this.version = version;
		this.revision = revision;
	}

	public GetFirmwareVersionResp(int ic, int version, int revision, int support) {
		this.ic = ic;
		this.version = version;
		this.revision = revision;
		this.support = support;
	}

	public int getRevision() {
		return revision;
	}

	public int getVersion() {
		return version;
	}

	public int getIc() {
		return ic;
	}

	public int getSupport() {
		return support;
	}

}

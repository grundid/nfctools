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

import org.nfctools.ndef.Record;

/**
 * 
 * Configuration of an external type record with namespace, class, encoder and decoder.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class ExternalTypeRecordConfig {

	private String namespace;
	private Class<? extends Record> recordClass;
	private ExternalTypeContentEncoder contentEncoder;
	private ExternalTypeContentDecoder contentDecoder;
	
	public ExternalTypeRecordConfig(String namespace, Class<? extends Record> recordClass, ExternalTypeContentEncoder contentEncoder, ExternalTypeContentDecoder contentDecoder) {
		this.namespace = namespace;
		this.recordClass = recordClass;
		this.contentEncoder = contentEncoder;
		this.contentDecoder = contentDecoder;
	}

	public String getNamespace() {
		return namespace;
	}
	public Class<? extends Record> getRecordClass() {
		return recordClass;
	}
	public ExternalTypeContentEncoder getContentEncoder() {
		return contentEncoder;
	}
	public ExternalTypeContentDecoder getContentDecoder() {
		return contentDecoder;
	}

	
}

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

import java.util.Collection;

import org.nfctools.ndef.Record;

/**
 * Implementation specification for a SNEP server servlet. You can implement this to perform NDEF messages exchanges
 * based on SNEP 1.0 specs.
 * 
 */
public interface Sneplet {

	/**
	 * Called if a client executes a GET request.
	 * 
	 * @param requestRecords
	 * @return response records
	 */
	Collection<Record> doGet(Collection<Record> requestRecords);

	/**
	 * Called if a client executes a PUT request.
	 * 
	 * @param requestRecords
	 */
	void doPut(Collection<Record> requestRecords);

}

/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
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

package org.nfctools.ndef.auri;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.decoder.AbstractTypeRecordDecoder;

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */
public class AbsoluteUriRecordDecoder extends AbstractTypeRecordDecoder<AbsoluteUriRecord> {

	public AbsoluteUriRecordDecoder() {
		super(NdefConstants.TNF_ABSOLUTE_URI, AbsoluteUriRecord.TYPE);
	}

	@Override
	protected AbsoluteUriRecord createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		String uri = new String(ndefRecord.getPayload(), NdefConstants.DEFAULT_CHARSET);
		return new AbsoluteUriRecord(uri);
	}
}

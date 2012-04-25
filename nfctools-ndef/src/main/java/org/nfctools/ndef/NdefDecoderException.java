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

package org.nfctools.ndef;

/**
 * Decoder exception for determining the source of the exception. 
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */
public class NdefDecoderException extends NdefException {

	private static final long serialVersionUID = 1L;
	
	private NdefRecord location;
	
	public NdefDecoderException(String message) {
		super(message);
	}
	
	public NdefDecoderException(String message, NdefRecord location) {
		super(message);
		
		this.location = location;
	}

	public NdefRecord getLocation() {
		return location;
	}
}

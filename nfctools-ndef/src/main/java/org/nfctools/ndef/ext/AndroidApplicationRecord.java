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

package org.nfctools.ndef.ext;

/**
 * Android Application Record.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */
public class AndroidApplicationRecord extends ExternalTypeRecord {

	/**
	 * An RTD indicating an Android Application Record.
	 */
	public static final String TYPE = "android.com:pkg";

	public AndroidApplicationRecord(String packageName) {
		setNamespace(TYPE);
		setContent(packageName);
	}

	public AndroidApplicationRecord() {
	}

	public String getPackageName() {
		return getContent();
	}

	public void setPackageName(String packageName) {
		setContent(packageName);
	}

	public boolean hasPackageName() {
		return hasContent();
	}
}

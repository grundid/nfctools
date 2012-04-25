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

	private static final String JAVA_PACKAGE_CONVENSION = "^[a-z]+(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$"; // http://checkstyle.sourceforge.net/config_naming.html

	private String packageName;

	public AndroidApplicationRecord(String packageName) {
		this.packageName = packageName;
	}

	public AndroidApplicationRecord() {
	}

	public boolean hasPackageName() {
		return packageName != null;
	}

	public boolean matchesNamingConvension() {
		return packageName.matches(JAVA_PACKAGE_CONVENSION);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AndroidApplicationRecord other = (AndroidApplicationRecord)obj;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		}
		else if (!packageName.equals(other.packageName))
			return false;
		return true;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}

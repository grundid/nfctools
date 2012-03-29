package org.nfctools.ndef.ext;

import org.nfctools.ndef.Record;

/**
 * @model
 */

public class AndroidApplicationRecord extends Record {

    /**
     * An RTD indicating an Android Application Record.
     */
    public static final byte[] TYPE = "android.com:pkg".getBytes();

	private String packageName;

	/**
	 * @model
	 * 
	 */

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public AndroidApplicationRecord(String packageName) {
		this.packageName = packageName;
	}

	public AndroidApplicationRecord() {
	}

	public boolean hasPackageName() {
		return packageName != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AndroidApplicationRecord other = (AndroidApplicationRecord) obj;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		return true;
	}
	
	
}

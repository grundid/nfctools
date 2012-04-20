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

/**
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class GeoRecord extends ExternalTypeRecord {

	/**
	 * An RTD indicating an Android Application Record.
	 */
	public static final String TYPE = "usingnfc.com:geo";

	private double latitude;
	private double longitude;
	private double altitude;

	private String addressInformation;
	
	public GeoRecord() {
		setNamespace(TYPE);
	}

	public GeoRecord(String addressInformation) {
		this();
		this.addressInformation = addressInformation;
	}

	public GeoRecord(double latitude, double longitude) {
		this();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GeoRecord(double latitude, double longitude, double altitude) {
		this(latitude, longitude);
		this.altitude = altitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public String getAddressInformation() {
		return addressInformation;
	}

	public void setAddressInformation(String addressInformation) {
		this.addressInformation = addressInformation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((addressInformation == null) ? 0 : addressInformation
						.hashCode());
		long temp;
		temp = Double.doubleToLongBits(altitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		GeoRecord other = (GeoRecord) obj;
		if (addressInformation == null) {
			if (other.addressInformation != null)
				return false;
		} else if (!addressInformation.equals(other.addressInformation))
			return false;
		if (Double.doubleToLongBits(altitude) != Double
				.doubleToLongBits(other.altitude))
			return false;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		return true;
	}




}
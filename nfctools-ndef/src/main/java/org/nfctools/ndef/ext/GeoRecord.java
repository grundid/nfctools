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
 * Geo record as defined by usingnfc.com. This is not an NFC Forum spec.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class GeoRecord extends ExternalTypeRecord {

	/**
	 * A namespace indicating an Geo Record.
	 */

	public static final String NAMESPACE = "usingnfc.com:geo";

	private Double latitude;
	private Double longitude;
	private Double altitude;

	private String addressInformation;

	public GeoRecord() {
	}
	
	public GeoRecord(String addressInformation) {
		this.addressInformation = addressInformation;
	}

	public GeoRecord(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GeoRecord(Double latitude, Double longitude, Double altitude) {
		this(latitude, longitude);
		this.altitude = altitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public String getAddressInformation() {
		return addressInformation;
	}

	public void setAddressInformation(String addressInformation) {
		this.addressInformation = addressInformation;
	}

	public boolean hasAddressInformation() {
		return addressInformation != null;
	}

	public boolean hasLongitude() {
		return longitude != null;
	}

	public boolean hasLatitude() {
		return latitude != null;
	}

	public boolean hasAltitude() {
		return altitude != null;
	}

	public boolean hasCoordinates() {
		return latitude != null && longitude != null;
	}
}
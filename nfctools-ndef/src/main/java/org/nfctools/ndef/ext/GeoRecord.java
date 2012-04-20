package org.nfctools.ndef.ext;

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
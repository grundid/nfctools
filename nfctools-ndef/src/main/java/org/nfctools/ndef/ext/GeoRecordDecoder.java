package org.nfctools.ndef.ext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Geo record decoder
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class GeoRecordDecoder implements ExternalTypeContentDecoder {

	// from the ZXing GeoResultParser
	private static final Pattern GEO_URL_PATTERN = Pattern.compile("([\\-0-9.]+),([\\-0-9.]+)(?:,([\\-0-9.]+))?(?:\\?(.*))?", Pattern.CASE_INSENSITIVE);

	@Override
	public ExternalTypeRecord decodeContent(String content) {

		Matcher matcher = GEO_URL_PATTERN.matcher(content);
		if (!matcher.matches()) {
			return null;
		}

		String addressInformation;
		String query = matcher.group(4);
		if(query != null) {
			if(!query.startsWith("q=")) {
				throw new IllegalArgumentException("Expected address information query starting with parameter 'q', found " + query);
			}
			addressInformation = query.substring(2);
		} else {
			addressInformation = null;
		}

		double latitude;
		double longitude;
		double altitude;
		try {
			latitude = Double.parseDouble(matcher.group(1));
			if (latitude > 90.0 || latitude < -90.0) {
				throw new IllegalArgumentException("Expected latitude within 90 positive or negative degrees, found " + latitude + " degrees.");
			}
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Expected float latitude, found '" + matcher.group(1) + "'"); // TODO ndef exception
		}
		try {
			longitude = Double.parseDouble(matcher.group(2));
			if (longitude > 180.0 || longitude < -180.0) {
				throw new IllegalArgumentException("Expected longitude within 180 positive or negative degrees, found " + longitude + " degrees.");
			}
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Expected float longitude, found '" + matcher.group(2) + "'"); // TODO ndef exception
		}

		if((longitude != 0.0 || latitude != 0.0) && addressInformation != null) {
			throw new IllegalArgumentException("Expected latitude and longitude coordinates or address information, not both.");
		}

		if (matcher.group(3) == null) {
			altitude = 0.0;
		} else {
			try {
				altitude = Double.parseDouble(matcher.group(3));
			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException("Expected float altitude, found '" + matcher.group(3) + "'"); // TODO ndef exception
			}
		}

		GeoRecord geoRecord = new GeoRecord();

		geoRecord.setAddressInformation(addressInformation);
		geoRecord.setLatitude(latitude);
		geoRecord.setLongitude(longitude);
		geoRecord.setAltitude(altitude);

		return geoRecord;
	}
}

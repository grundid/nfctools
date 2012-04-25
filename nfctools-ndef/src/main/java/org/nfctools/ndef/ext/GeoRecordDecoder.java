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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nfctools.ndef.NdefDecoderException;

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
				throw new NdefDecoderException("Expected address information query starting with parameter 'q', found " + query);
			}
			addressInformation = query.substring(2);
		} else {
			addressInformation = null;
		}

		Double latitude;
		Double longitude;
		Double altitude;
		try {
			latitude = Double.parseDouble(matcher.group(1));
			if (latitude > 90.0 || latitude < -90.0) {
				throw new NdefDecoderException("Expected latitude within 90 positive or negative degrees, found " + latitude + " degrees.");
			}
		} catch (NumberFormatException nfe) {
			throw new NdefDecoderException("Expected float latitude, found '" + matcher.group(1) + "'");
		}
		try {
			longitude = Double.parseDouble(matcher.group(2));
			if (longitude > 180.0 || longitude < -180.0) {
				throw new NdefDecoderException("Expected longitude within 180 positive or negative degrees, found " + longitude + " degrees.");
			}
		} catch (NumberFormatException nfe) {
			throw new NdefDecoderException("Expected float longitude, found '" + matcher.group(2) + "'");
		}

		if((longitude.doubleValue() != 0.0 || latitude.doubleValue() != 0.0) && addressInformation != null) {
			throw new NdefDecoderException("Expected latitude and longitude coordinates or address information, not both.");
		}

		if (matcher.group(3) == null) {
			altitude = null;
		} else {
			try {
				altitude = Double.parseDouble(matcher.group(3));
			} catch (NumberFormatException nfe) {
				throw new NdefDecoderException("Expected float altitude, found '" + matcher.group(3) + "'");
			}
		}

		GeoRecord geoRecord = new GeoRecord();

		if(addressInformation != null) {
			geoRecord.setAddressInformation(addressInformation);
		} else {
			geoRecord.setLatitude(latitude);
			geoRecord.setLongitude(longitude);
		}
		geoRecord.setAltitude(altitude);

		return geoRecord;
	}
}

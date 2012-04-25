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

import org.nfctools.ndef.NdefEncoderException;

/**
 * 
 * Geo record encoder.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class GeoRecordEncoder implements ExternalTypeContentEncoder {

	// taken from http://stackoverflow.com/questions/724043/http-url-address-encoding-in-java
    public static String encode(String input) {
        StringBuilder resultStr = new StringBuilder();
        for (char ch : input.toCharArray()) {
        	if(ch == ' ') {
        		resultStr.append('+'); // TODO check for more than one plus 
        	} else if (isUnsafe(ch)) {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            } else {
                resultStr.append(ch);
            }
        }
        return resultStr.toString();
    }

    private static char toHex(int ch) {
        return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    private static boolean isUnsafe(char ch) {
        if (ch > 128 || ch < 0)
            return true;
        return "%$&+,/:;=?@<>#%".indexOf(ch) != -1;
    }
    
	@Override
	public String encodeContent(ExternalTypeRecord externalType) {
		
		GeoRecord geoRecord = (GeoRecord)externalType;

		StringBuilder result = new StringBuilder();

		// if coordinates, then no address information and the opposite.
		if(geoRecord.hasCoordinates()) {
			if(geoRecord.hasAddressInformation()) {
				throw new NdefEncoderException("Expected latitude and longitude coordinates or address information, not both.", externalType);
			}
			
			Double latitude = geoRecord.getLatitude();
			if (latitude.doubleValue() > 90.0 || latitude.doubleValue() < -90.0) {
				throw new NdefEncoderException("Expected latitude within 90 positive or negative degrees, found " + latitude + " degrees.", externalType);
			}
			Double longitude = geoRecord.getLongitude();
			if (longitude.doubleValue() > 180.0 || longitude.doubleValue() < -180.0) {
				throw new NdefEncoderException("Expected longitude within 180 positive or negative degrees, found " + longitude + " degrees.", externalType);
			}

			result.append(latitude.toString());
			result.append(',');
			result.append(longitude.toString());
		}  else if(geoRecord.hasLatitude()) {
			throw new NdefEncoderException("Expected longitude coordinate set when latitude coordinate set.", externalType);
		}  else if(geoRecord.hasLongitude()) {
			throw new NdefEncoderException("Expected latitude coordinate set when longitude coordinate set.", externalType);
		} else if(!geoRecord.hasAddressInformation()) {
			throw new NdefEncoderException("Expected coordinates or address information set.", externalType);
		} else {
			result.append("0,0");
		}

		if (geoRecord.hasAltitude()) {
			result.append(',');
			result.append(geoRecord.getAltitude().toString());
		}
		
		if(geoRecord.hasAddressInformation()) {
			result.append("?q=");
			result.append(encode(geoRecord.getAddressInformation()));
		}
		
		return result.toString();
	}

}

package org.nfctools.ndef.ext;

import java.net.URLEncoder;


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
		
		double latitude = geoRecord.getLatitude();
		if (latitude > 90.0 || latitude < -90.0) {
			throw new IllegalArgumentException("Expected latitude within 90 positive or negative degrees, found " + latitude + " degrees.");
		}
		double longitude = geoRecord.getLongitude();
		if (longitude > 180.0 || longitude < -180.0) {
			throw new IllegalArgumentException("Expected longitude within 180 positive or negative degrees, found " + longitude + " degrees.");
		}

		String addressInformation = geoRecord.getAddressInformation();
		
		if((longitude != 0.0 || latitude != 0.0) && addressInformation != null) {
			throw new IllegalArgumentException("Expected latitude and longitude coordinates or address information, not both.");
		}

		StringBuilder result = new StringBuilder();
		result.append(latitude);
		result.append(',');
		result.append(longitude);
		
		double altitude = geoRecord.getAltitude();
		if (altitude > 0) {
			result.append(',');
			result.append(altitude);
		}
		
		if(addressInformation != null) {
			result.append("?q=");
			result.append(encode(addressInformation));
		}
		
		return result.toString();
	}
	


}

package org.nfctools.spi.acs;

import java.io.IOException;

public class ApduException extends IOException {

	public ApduException(String message) {
		super(message);
	}
}

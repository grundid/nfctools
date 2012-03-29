package org.nfctools;

import java.io.IOException;

public class NfcTimeoutException extends IOException {

	public NfcTimeoutException() {
	}

	public NfcTimeoutException(String message) {
		super(message);
	}
}

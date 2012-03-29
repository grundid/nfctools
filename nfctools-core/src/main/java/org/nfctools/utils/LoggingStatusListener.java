package org.nfctools.utils;

import org.nfctools.scio.TerminalStatus;
import org.nfctools.scio.TerminalStatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingStatusListener implements TerminalStatusListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void onStatusChanged(TerminalStatus status) {
		log.info("Status: " + status);
	}
}

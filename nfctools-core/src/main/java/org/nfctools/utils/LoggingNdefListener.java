package org.nfctools.utils;

import java.util.Collection;

import org.nfctools.ndef.NdefListener;
import org.nfctools.ndef.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingNdefListener implements NdefListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void onNdefMessages(Collection<Record> records) {
		for (Record record : records) {
			log.info(record.toString());
		}
	}
}

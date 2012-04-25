package org.nfctools.utils;

import org.nfctools.api.Tag;
import org.nfctools.api.UnknownTagListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUnknownTagListener implements UnknownTagListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void unsupportedTag(Tag tag) {
		log.info("Unkown tag: " + tag + " " + NfcUtils.convertBinToASCII(tag.getGeneralBytes()));
	}
}

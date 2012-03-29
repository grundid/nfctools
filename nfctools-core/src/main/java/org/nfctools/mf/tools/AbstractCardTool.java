package org.nfctools.mf.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.mad.MadConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCardTool {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected Collection<byte[]> knownKeys = new ArrayList<byte[]>();

	public AbstractCardTool() {
		knownKeys.add(MfConstants.TRANSPORT_KEY);
		knownKeys.add(MfConstants.NDEF_KEY);
		knownKeys.add(MadConstants.DEFAULT_MAD_KEY);
		//		knownKeys.add(NfcUtils.convertASCIIToBin("AABBCCDDEEFF"));
	}

	public void addKnownKey(byte[] key) {
		knownKeys.add(key);
	}

	public abstract void doWithCard(MfCard card, MfReaderWriter readerWriter) throws IOException;

}

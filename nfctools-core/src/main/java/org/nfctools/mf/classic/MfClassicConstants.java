package org.nfctools.mf.classic;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.mad.MadConstants;

public class MfClassicConstants {

	public static final KeyValue NDEF_KEY = new KeyValue(Key.A, MfConstants.NDEF_KEY);
	public static final KeyValue MAD_KEY = new KeyValue(Key.A, MadConstants.DEFAULT_MAD_KEY);
	public static final KeyValue TRANSPORT_KEY = new KeyValue(Key.A, MfConstants.TRANSPORT_KEY);
}

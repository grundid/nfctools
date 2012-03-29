package org.nfctools.mf.ndef;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.mad.ApplicationId;

public class MfNdefConstants {

	public static final ApplicationId NDEF_APP_ID = new ApplicationId(MfConstants.NDEF_APPLICATION_CODE,
			MfConstants.NDEF_FUNCTION_CLUSTER_CODE);
}

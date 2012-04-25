package org.nfctools;

import org.nfctools.ndef.NdefOperations;

public interface NdefTagListener {

	void onNdefTag(NdefOperations ndefOperations);
}

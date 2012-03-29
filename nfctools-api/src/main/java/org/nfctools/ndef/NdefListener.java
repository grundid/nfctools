package org.nfctools.ndef;

import java.util.Collection;

public interface NdefListener {

	void onNdefMessages(Collection<Record> records);
}

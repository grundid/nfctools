package org.nfctools.nfcip;

import java.io.IOException;

public interface NFCIPConnectionListener {

	void onConnection(NFCIPConnection connection) throws IOException;
}

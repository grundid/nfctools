package org.nfctools.nfcip;

import java.io.IOException;

public interface NFCIPCommunicator {

	<RESP, REQ> RESP sendMessage(REQ request) throws IOException;
}

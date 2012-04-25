package org.nfctools.api;

import org.nfctools.scio.Command;
import org.nfctools.scio.Response;

public interface ApduTag {

	Response transmit(Command command);
}

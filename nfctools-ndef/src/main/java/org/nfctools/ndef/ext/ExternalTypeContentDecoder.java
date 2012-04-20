package org.nfctools.ndef.ext;

public interface ExternalTypeContentDecoder {

	ExternalTypeRecord decodeContent(String content);

}

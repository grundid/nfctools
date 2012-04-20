package org.nfctools.ndef.ext;

public class AndroidApplicationRecordDecoder implements ExternalTypeContentDecoder {

	@Override
	public ExternalTypeRecord decodeContent(String content) {
		
		return new AndroidApplicationRecord(content);
		
	}

}

package org.nfctools.ndef.ext;

public class AndroidApplicationRecordEncoder implements ExternalTypeContentEncoder {

	@Override
	public String encodeContent(ExternalTypeRecord externalType) {
		
		AndroidApplicationRecord androidApplicationRecord = (AndroidApplicationRecord)externalType;
		
		return androidApplicationRecord.getPackageName();
	}

}

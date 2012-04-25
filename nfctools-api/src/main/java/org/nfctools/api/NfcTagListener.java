package org.nfctools.api;

public interface NfcTagListener {

	boolean canHandle(Tag tag);

	void handleTag(Tag tag);
}

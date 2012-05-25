package org.nfctools.snep;

public interface SnepAgentListener {

	boolean hasDataToSend();

	void onSnepConnection(SnepAgent snepAgent);
}

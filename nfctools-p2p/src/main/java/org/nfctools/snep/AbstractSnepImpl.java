package org.nfctools.snep;

import org.nfctools.llcp.Llcp;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.llcp.LlcpSocket;
import org.nfctools.llcp.ServiceAccessPoint;

public abstract class AbstractSnepImpl implements ServiceAccessPoint {

	protected byte snepVersion = 0x10;
	protected int maxInformationUnit = LlcpConstants.DEFAULT_MIU;

	protected FragmentReader reader = new FragmentReader();
	protected FragmentIterator fragmentIterator;
	protected SnepMessage continueMessage;

	protected AbstractSnepImpl(byte continueMessageCode) {
		continueMessage = new SnepMessage(snepVersion, continueMessageCode);
	}

	@Override
	public void onLlcpActive(Llcp llcp) {
	}

	@Override
	public void onConnectFailed() {
	}

	@Override
	public void onConnectSucceeded(LlcpSocket llcpSocket) {
	}

	@Override
	public void onSendFailed() {
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public void onConnectionActive(LlcpSocket llcpSocket) {
	}

	@Override
	public byte[] onInformation(byte[] serviceDataUnit) {

		reader.addFragment(serviceDataUnit);

		if (reader.isComplete()) {
			byte[] completeMessage = reader.getCompleteMessage();
			reader.reset();
			SnepMessage snepMessage = new SnepMessage(completeMessage);
			if (snepMessage.getVersion() == snepVersion)
				return processMessage(snepMessage);
			else
				return new SnepMessage(snepVersion, Response.UNSUPPORTED_VERSION).getBytes();
		}
		else {
			if (reader.isFirstFragment()) {
				return continueMessage.getBytes();
			}
			else
				return null;
		}
	}

	protected abstract byte[] processMessage(SnepMessage snepMessage);

}

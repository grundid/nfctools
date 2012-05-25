package org.nfctools.snep;

import java.util.List;

import org.nfctools.llcp.Llcp;
import org.nfctools.llcp.LlcpSocket;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnepClient extends AbstractSnepImpl {

	private Logger log = LoggerFactory.getLogger(getClass());
	private SnepAgentListener snepAgentListener;
	private SnepRequestContainer snepRequestContainer = new SnepRequestContainer();
	private boolean connected = false;

	public SnepClient() {
		super(Request.CONTINUE.getCode());
	}

	public void setSnepAgentListener(SnepAgentListener snepAgentListener) {
		this.snepAgentListener = snepAgentListener;
	}

	@Override
	public void onLlcpActive(Llcp llcp) {
		if (snepAgentListener != null && snepAgentListener.hasDataToSend() && !connected) {
			log.debug("Connecting to " + SnepConstants.SNEP_SERVICE_NAME);
			llcp.connectToService(SnepConstants.SNEP_SERVICE_NAME, this);
		}
	}

	@Override
	public void onConnectionActive(LlcpSocket llcpSocket) {
		if (snepAgentListener.hasDataToSend())
			handleActiveConnection(llcpSocket);
		else {
			log.debug("No data to send, disconnecting...");
			llcpSocket.disconnect();
		}
	}

	@Override
	public void onConnectSucceeded(LlcpSocket llcpSocket) {
		log.debug("Connection succeeded");
		connected = true;
		handleActiveConnection(llcpSocket);
	}

	private void handleActiveConnection(LlcpSocket llcpSocket) {
		snepAgentListener.onSnepConnection(snepRequestContainer);

		if (snepRequestContainer.hasRequest()) {
			maxInformationUnit = llcpSocket.getMaximumInformationUnit();
			byte[] responseMessage = processSnepRequestContainer();
			llcpSocket.sendMessage(responseMessage);
		}
	}

	@Override
	public void onDisconnect() {
		log.debug("Disconnect succeeded");
		connected = false;
	}

	private byte[] processSnepRequestContainer() {
		byte[] encodedRecords = NdefContext.getNdefMessageEncoder().encode(snepRequestContainer.getRecords());
		SnepMessage snepMessage = new SnepMessage(snepVersion, snepRequestContainer.getRequest());
		snepMessage.setInformation(encodedRecords);
		fragmentIterator = new FragmentIterator(snepMessage.getBytes(), maxInformationUnit);
		byte[] responseMessage = fragmentIterator.next();
		return responseMessage;
	}

	@Override
	public void onSendSucceeded(LlcpSocket llcpSocket) {
		if (fragmentIterator.hasNext()) {
			llcpSocket.sendMessage(fragmentIterator.next());
		}
	}

	@Override
	public boolean canAcceptConnection(Object[] parameters) {
		return false;
	}

	@Override
	protected byte[] processMessage(SnepMessage snepMessage) {

		if (snepMessage.getMessageCode() == Response.CONTINUE.getCode()) {
			if (fragmentIterator != null && fragmentIterator.hasNext())
				return fragmentIterator.next();
		}
		else if (snepMessage.getMessageCode() == Response.SUCCESS.getCode()) {
			List<Record> records = NdefContext.getNdefMessageDecoder().decodeToRecords(snepMessage.getInformation());
			if (snepRequestContainer.hasRequest()) {
				snepRequestContainer.handleSuccess(records);
				if (snepRequestContainer.hasRequest()) {
					return processSnepRequestContainer();
				}
			}
		}
		else {
			if (snepRequestContainer.hasRequest()) {
				snepRequestContainer.handleFailure();
			}
		}

		// TODO handle other responses
		return null;
	}

}

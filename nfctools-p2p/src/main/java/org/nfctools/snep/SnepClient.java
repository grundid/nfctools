package org.nfctools.snep;

import java.util.List;

import org.nfctools.llcp.Llcp;
import org.nfctools.llcp.LlcpSocket;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.Record;

public class SnepClient extends AbstractSnepImpl {

	private SnepAgentListener snepAgentListener;
	private GetResponseListener getResponseListener;
	private boolean connected = false;

	public SnepClient() {
		super(Request.CONTINUE.getCode());
	}

	public void setSnepAgentListener(SnepAgentListener snepAgentListener) {
		this.snepAgentListener = snepAgentListener;
	}

	@Override
	public void onLlcpActive(Llcp llcp) {
		if (snepAgentListener != null && !connected) {
			llcp.connectToService(SnepConstants.SNEP_SERVICE_NAME, this);
		}
	}

	@Override
	public void onConnectSucceeded(LlcpSocket llcpSocket) {
		connected = true;
		SnepRequestContainer requestContainer = new SnepRequestContainer();
		snepAgentListener.onSnepConnection(requestContainer);

		if (requestContainer.hasRequest()) {
			maxInformationUnit = llcpSocket.getMaximumInformationUnit();
			byte[] responseMessage = processSnepRequestContainer(requestContainer);
			llcpSocket.sendMessage(responseMessage);
		}
	}

	@Override
	public void onDisconnect() {
		connected = false;
	}

	private byte[] processSnepRequestContainer(SnepRequestContainer requestContainer) {
		getResponseListener = requestContainer.getGetResponseListener();
		byte[] encodedRecords = NdefContext.getNdefMessageEncoder().encode(requestContainer.getRecords());
		SnepMessage snepMessage = new SnepMessage(snepVersion, requestContainer.getRequest());
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
			if (getResponseListener != null) {
				SnepRequestContainer requestContainer = new SnepRequestContainer();
				getResponseListener.onGetResponse(records, requestContainer);
				if (requestContainer.hasRequest()) {
					return processSnepRequestContainer(requestContainer);
				}
			}
		}

		// TODO handle other responses
		return null;
	}

}

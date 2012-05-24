package org.nfctools.snep;

import java.util.Collection;
import java.util.List;

import org.nfctools.llcp.LlcpConstants;
import org.nfctools.llcp.LlcpSocket;
import org.nfctools.llcp.LlcpUtils;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.Record;

public class SnepServer extends AbstractSnepImpl {

	private Sneplet sneplet;

	public SnepServer(Sneplet sneplet) {
		super(Response.CONTINUE.getCode());
		this.sneplet = sneplet;
	}

	@Override
	public void onSendSucceeded(LlcpSocket llcpSocket) {
		if (fragmentIterator != null && fragmentIterator.hasNext())
			llcpSocket.sendMessage(fragmentIterator.next());
	}

	@Override
	public boolean canAcceptConnection(Object[] parameters) {
		maxInformationUnit = LlcpConstants.DEFAULT_MIU + LlcpUtils.getMiuExtension(parameters);
		return true;
	}

	@Override
	protected byte[] processMessage(SnepMessage snepMessage) {

		if (snepMessage.getMessageCode() == Request.PUT.getCode()) {
			List<Record> records = NdefContext.getNdefMessageDecoder().decodeToRecords(snepMessage.getInformation());
			sneplet.doPut(records);
			SnepMessage successMessage = new SnepMessage(snepVersion, Response.SUCCESS);
			return successMessage.getBytes();
		}
		else if (snepMessage.getMessageCode() == Request.GET.getCode()) {
			List<Record> records = NdefContext.getNdefMessageDecoder().decodeToRecords(snepMessage.getInformation());

			Collection<Record> responseRecords = sneplet.doGet(records);

			SnepMessage successMessage = new SnepMessage(snepVersion, Response.SUCCESS);
			successMessage.setInformation(NdefContext.getNdefMessageEncoder().encode(responseRecords));

			fragmentIterator = new FragmentIterator(successMessage.getBytes(), maxInformationUnit);
			return fragmentIterator.next();
		}
		else if (snepMessage.getMessageCode() == Request.CONTINUE.getCode()) {
			if (fragmentIterator != null && fragmentIterator.hasNext())
				return fragmentIterator.next();
			else
				return new SnepMessage(snepVersion, Response.BAD_REQUEST).getBytes();
		}
		else
			return new SnepMessage(snepVersion, Response.NOT_IMPLEMENTED).getBytes();
	}
}

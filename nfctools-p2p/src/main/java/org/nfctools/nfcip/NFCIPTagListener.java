package org.nfctools.nfcip;

import java.io.IOException;

import org.nfctools.api.ApduTag;
import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagType;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.spi.acs.ApduTagReaderWriter;
import org.nfctools.spi.tama.nfcip.TamaNfcIpCommunicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NFCIPTagListener implements NfcTagListener {

	private Logger log = LoggerFactory.getLogger(getClass());
	private NFCIPConnectionListener nfcipConnectionListener;

	public NFCIPTagListener(NFCIPConnectionListener nfcipConnectionListener) {
		this.nfcipConnectionListener = nfcipConnectionListener;
	}

	@Override
	public boolean canHandle(Tag tag) {
		return tag.getTagType().equals(TagType.NFCIP);
	}

	@Override
	public void handleTag(Tag tag) {
		ApduTagReaderWriter apduReaderWriter = new ApduTagReaderWriter((ApduTag)tag);

		TamaNfcIpCommunicator nfcIpCommunicator = new TamaNfcIpCommunicator(apduReaderWriter, apduReaderWriter);
		nfcIpCommunicator.setNfcId(LlcpConstants.nfcId3t);
		nfcIpCommunicator.setFelicaParams(LlcpConstants.felicaParams);
		nfcIpCommunicator.setMifareParams(LlcpConstants.mifareParams);
		nfcIpCommunicator.setGeneralBytes(LlcpConstants.initiatorGeneralBytes);
		try {
			NFCIPConnection nfcipConnection = nfcIpCommunicator.connectAsInitiator();
			log.info("Connection: " + nfcipConnection);
			handleNfcipConnection(nfcipConnection);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void handleNfcipConnection(NFCIPConnection nfcipConnection) throws IOException {
		if (nfcipConnection != null && nfcipConnectionListener != null) {
			nfcipConnectionListener.onConnection(nfcipConnection);
		}
	}
}

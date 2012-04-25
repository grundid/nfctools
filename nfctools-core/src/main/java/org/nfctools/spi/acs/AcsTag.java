package org.nfctools.spi.acs;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import org.nfctools.NfcException;
import org.nfctools.api.ApduTag;
import org.nfctools.api.Tag;
import org.nfctools.api.TagType;
import org.nfctools.scio.Command;
import org.nfctools.scio.Response;

public class AcsTag extends Tag implements ApduTag {

	private Card card;
	private CardChannel cardChannel;

	public AcsTag(TagType tagType, byte[] generalBytes, Card card) {
		super(tagType, generalBytes);
		this.card = card;
		cardChannel = card.getBasicChannel();
	}

	public Card getCard() {
		return card;
	}

	@Override
	public Response transmit(Command command) {
		try {
			CommandAPDU commandAPDU = command.hasData() ? new CommandAPDU(Apdu.CLS_PTS, command.getInstruction(),
					command.getP1(), command.getP2(), command.getData()) : new CommandAPDU(Apdu.CLS_PTS,
					command.getInstruction(), command.getP1(), command.getP2(), command.getLength());
			ResponseAPDU responseAPDU = cardChannel.transmit(commandAPDU);
			return new Response(responseAPDU.getSW1(), responseAPDU.getSW2(), responseAPDU.getData());
		}
		catch (CardException e) {
			throw new NfcException(e);
		}
	}
}

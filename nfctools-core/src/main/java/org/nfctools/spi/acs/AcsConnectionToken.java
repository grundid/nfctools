package org.nfctools.spi.acs;

import javax.smartcardio.Card;

public class AcsConnectionToken {

	private Card card;

	public AcsConnectionToken(Card card) {
		this.card = card;
	}

	public Card getCard() {
		return card;
	}

}

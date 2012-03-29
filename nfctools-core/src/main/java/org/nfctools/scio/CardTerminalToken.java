package org.nfctools.scio;

import javax.smartcardio.CardTerminal;

public class CardTerminalToken {

	private CardTerminal cardTerminal;

	public CardTerminal getCardTerminal() {
		return cardTerminal;
	}

	void setCardTerminal(CardTerminal cardTerminal) {
		this.cardTerminal = cardTerminal;
	}

}

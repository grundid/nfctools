package org.nfctools.mf;

import org.nfctools.mf.card.MfCard;

public class SimpleMfAccess {

	private MfCard card;
	private Key key;
	private byte[] keyValue;

	public SimpleMfAccess(MfCard card, Key key, byte[] keyValue) {
		this.card = card;
		this.key = key;
		this.keyValue = keyValue;
	}

	public MfCard getCard() {
		return card;
	}

	public Key getKey() {
		return key;
	}

	public byte[] getKeyValue() {
		return keyValue;
	}
}

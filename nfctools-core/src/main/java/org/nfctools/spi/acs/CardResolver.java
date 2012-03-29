package org.nfctools.spi.acs;

import javax.smartcardio.Card;

import org.nfctools.mf.MfException;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.card.MfCard1k;
import org.nfctools.mf.card.MfCard4k;
import org.nfctools.utils.NfcUtils;

public class CardResolver {

	public MfCard resolvecard(Card card) throws MfException {

		byte[] historicalBytes = card.getATR().getHistoricalBytes();

		if (historicalBytes.length == 15) {

			AcsConnectionToken connectionToken = new AcsConnectionToken(card);

			switch (historicalBytes[10]) {
			case 1:
				return new MfCard1k(new byte[0], connectionToken);
			case 2:
				return new MfCard4k(new byte[0], connectionToken);
			}
		}

		throw new MfException("unknown card type: " + NfcUtils.convertBinToASCII(historicalBytes));

	}

}

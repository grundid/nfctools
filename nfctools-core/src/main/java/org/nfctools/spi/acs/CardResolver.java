/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

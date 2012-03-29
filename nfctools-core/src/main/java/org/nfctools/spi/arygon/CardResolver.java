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
package org.nfctools.spi.arygon;

import org.nfctools.mf.MfException;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.card.MfCard1k;
import org.nfctools.mf.card.MfCard4k;

public class CardResolver {

	public MfCard resolvecard(int type, byte[] nfcId, byte targetNumber) throws MfException {

		ArygonConnectionToken connectionToken = new ArygonConnectionToken(targetNumber);

		switch (type) {
		case 0x08:
			return new MfCard1k(nfcId, connectionToken);
		case 0x18:
			return new MfCard4k(nfcId, connectionToken);
		case 0x20:
			throw new MfException("DESFire not supported yet");
		}
		throw new MfException("unknown card type " + type);
	}
}

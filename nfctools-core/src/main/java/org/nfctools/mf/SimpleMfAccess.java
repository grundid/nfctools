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
package org.nfctools.mf;

import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.classic.Key;

@Deprecated
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

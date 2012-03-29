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
package org.nfctools.utils;

import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

public class CardTerminalUtils {

	public static CardTerminal getTerminalByName(String terminalName) {
		try {
			TerminalFactory terminalFactory = TerminalFactory.getDefault();

			List<CardTerminal> terminals = terminalFactory.terminals().list();
			for (CardTerminal terminal : terminals) {
				if (terminal.getName().contains(terminalName)) {
					return terminal;
				}
			}
		}
		catch (CardException e) {
			throw new RuntimeException(e);
		}

		throw new IllegalArgumentException("no card terminal found, expected: [" + terminalName + "], available: ["
				+ getAvailableTerminals() + "]");

	}

	private static String getAvailableTerminals() {
		StringBuilder sb = new StringBuilder();
		TerminalFactory terminalFactory = TerminalFactory.getDefault();

		try {
			List<CardTerminal> terminals = terminalFactory.terminals().list();
			for (CardTerminal terminal : terminals) {
				if (sb.length() != 0) {
					sb.append(", ");
				}
				sb.append(terminal.getName());
			}
		}
		catch (CardException e) {
		}
		return sb.toString();
	}

}

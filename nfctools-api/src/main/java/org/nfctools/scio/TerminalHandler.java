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
package org.nfctools.scio;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

public class TerminalHandler {

	private Collection<Terminal> knownTerminals = new HashSet<Terminal>();

	public void addTerminal(Terminal terminal) {
		knownTerminals.add(terminal);
	}

	public Terminal getAvailableTerminal() {
		try {
			TerminalFactory terminalFactory = TerminalFactory.getDefault();

			List<CardTerminal> terminals = terminalFactory.terminals().list();
			for (CardTerminal terminal : terminals) {
				for (Terminal knownTerminal : knownTerminals) {
					if (knownTerminal.canHandle(terminal.getName())) {
						knownTerminal.setCardTerminal(terminal);
						return knownTerminal;
					}
				}
			}
		}
		catch (CardException e) {
			throw new RuntimeException(e);
		}

		throw new IllegalArgumentException("no supported card terminal found");

	}
}

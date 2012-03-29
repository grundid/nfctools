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

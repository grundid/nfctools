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

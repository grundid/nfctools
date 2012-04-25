package org.nfctools.spi.acs;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import org.nfctools.api.TagListener;
import org.nfctools.api.TagType;
import org.nfctools.scio.TerminalStatus;
import org.nfctools.scio.TerminalStatusListener;

public class InitiatorTerminalTagScanner implements Runnable {

	private CardTerminal cardTerminal;
	private TerminalStatusListener statusListener;
	private TagListener tagListener;

	public InitiatorTerminalTagScanner(CardTerminal cardTerminal, TerminalStatusListener statusListener, TagListener tagListener) {
		this.cardTerminal = cardTerminal;
		this.statusListener = statusListener;
		this.tagListener = tagListener;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			notifyStatus(TerminalStatus.WAITING);
			try {
				if (cardTerminal.waitForCardPresent(500)) {
					Card card = null;
					try {
						card = cardTerminal.connect("*");
						handleCard(card);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						if (card != null) {
							card.disconnect(true);
						}
						try {
							while (cardTerminal.isCardPresent()) {
								try {
									Thread.sleep(500);
								}
								catch (InterruptedException e) {
									break;
								}
							}
							cardTerminal.waitForCardAbsent(1000);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						notifyStatus(TerminalStatus.DISCONNECTED);
					}
				}
			}
			catch (CardException e) {
			}
		}
	}

	private void handleCard(Card card) {
		TagType tagType = TagType.UKNOWN;
		byte[] historicalBytes = card.getATR().getHistoricalBytes();

		int tagId = historicalBytes[9] << 8 | historicalBytes[10];

		switch (tagId) {
			case 0x0001:
				tagType = TagType.MIFARE_CLASSIC_1K;
				break;
			case 0x0002:
				tagType = TagType.MIFARE_CLASSIC_4K;
				break;
			case 0x0003:
				tagType = TagType.MIFARE_ULTRALIGHT;
				break;
			case 0x0026:
				tagType = TagType.MIFARE_MINI;
				break;
			case 0xF004:
				tagType = TagType.TOPAZ_JEWEL;
				break;
			case 0xF011:
				tagType = TagType.FELICA_212K;
				break;
			case 0xF012:
				tagType = TagType.FELICA_424K;
				break;
			case 0xFF40:
				tagType = TagType.NFCIP;
				break;
		}

		tagListener.onTag(new AcsTag(tagType, historicalBytes, card));
	}

	private void notifyStatus(TerminalStatus status) {
		if (statusListener != null)
			statusListener.onStatusChanged(status);
	}

}

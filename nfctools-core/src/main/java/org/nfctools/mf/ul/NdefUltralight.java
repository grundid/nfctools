package org.nfctools.mf.ul;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;

import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.tlv.NdefMessageTlv;
import org.nfctools.mf.tlv.Tlv;
import org.nfctools.mf.tlv.TypeLengthValueReader;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessage;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.Record;
import org.nfctools.scio.Terminal;
import org.nfctools.spi.acs.AcrMfUlReaderWriter;
import org.nfctools.spi.acs.AcsTerminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class NdefUltralight {

	private static Logger log = LoggerFactory.getLogger(NdefUltralight.class.getName());

	private Terminal terminal = new AcsTerminal();
	private NdefMessageDecoder decoder = NdefContext.getNdefMessageDecoder();

	public void runNdefReader() {
		CardTerminal cardTerminal = terminal.getCardTerminal();
		MfUlReaderWriter readerWriter = new AcrMfUlReaderWriter();

		while (!Thread.interrupted()) {
			try {
				System.out.println("Waiting...");
				if (cardTerminal.waitForCardPresent(500)) {
					Card card = cardTerminal.connect("*");
					MfBlock[] blocks = readerWriter.readBlock(card, 4, 38);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					for (int x = 0; x < blocks.length; x++) {

						System.out.print("[" + zero(x) + "] ");
						byte[] data = blocks[x].getData();
						for (byte b : data)
							System.out.print(zero(b) + " ");

						System.out.println();
						baos.write(data);
					}
					TypeLengthValueReader reader = new TypeLengthValueReader(new ByteArrayInputStream(
							baos.toByteArray()));

					while (reader.hasNext()) {
						Tlv tlv = reader.next();
						if (tlv instanceof NdefMessageTlv) {
							NdefMessage ndefMessage = decoder.decode(((NdefMessageTlv)tlv).getNdefMessage());
							for (Record record : decoder.decodeToRecords(ndefMessage)) {
								System.out.println(record);
							}
						}

					}

					return;
				}

				while (cardTerminal.isCardPresent()) {
					Thread.sleep(500);
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private String zero(int x) {
		String s = Integer.toHexString(x & 0xFF);
		if (s.length() == 1)
			return "0" + s;
		else
			return s;
	}

	public static void main(String[] args) {

		try {
			NdefUltralight demo = new NdefUltralight();
			demo.runNdefReader();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}

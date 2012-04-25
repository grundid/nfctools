package org.nfctools.utils;

import java.io.IOException;

import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.tlv.NdefMessageTlv;
import org.nfctools.mf.tlv.Tlv;
import org.nfctools.mf.tlv.TypeLengthValueReader;
import org.nfctools.mf.ul.MemoryLayout;
import org.nfctools.mf.ul.MfUlReaderWriter;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessage;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.Record;
import org.nfctools.tags.TagInputStream;

public class TagUtils {

	private static NdefMessageDecoder decoder = NdefContext.getNdefMessageDecoder();

	public static void outputContents(MfUlReaderWriter readerWriter, MemoryLayout memoryLayout) throws IOException {
		outputBinaryContent(readerWriter, memoryLayout);
		outputNdefMessage(readerWriter, memoryLayout);
	}

	public static void outputBinaryContent(MfUlReaderWriter readerWriter, MemoryLayout memoryLayout) throws IOException {
		MfBlock[] blocks = readerWriter.readBlock(memoryLayout.getFirstPage(), memoryLayout.getLastPage()
				- memoryLayout.getFirstPage() + 1);
		for (int x = 0; x < blocks.length; x++) {

			System.out.print("[" + zero(x + memoryLayout.getFirstPage()) + "]   ");
			byte[] data = blocks[x].getData();
			for (byte b : data)
				System.out.print(zero(b) + " ");

			System.out.println();
		}
	}

	public static void outputNdefMessage(MfUlReaderWriter readerWriter, MemoryLayout memoryLayout) {
		TypeLengthValueReader reader = new TypeLengthValueReader(new TagInputStream(memoryLayout, readerWriter));

		while (reader.hasNext()) {
			Tlv tlv = reader.next();
			if (tlv instanceof NdefMessageTlv) {
				NdefMessage ndefMessage = decoder.decode(((NdefMessageTlv)tlv).getNdefMessage());
				for (Record record : decoder.decodeToRecords(ndefMessage)) {
					System.out.println(record);
				}
			}
		}
	}

	private static String zero(int x) {
		String s = Integer.toHexString(x & 0xFF);
		if (s.length() == 1)
			return "0" + s;
		else
			return s;
	}
}

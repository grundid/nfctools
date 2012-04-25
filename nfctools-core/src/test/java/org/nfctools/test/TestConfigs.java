package org.nfctools.test;

import org.nfctools.mf.ul.MemoryLayout;
import org.nfctools.mf.ul.Type2NdefOperations;
import org.nfctools.spi.acs.AcrMfUlReaderWriter;

public abstract class TestConfigs {

	public static final String[] TYPE2_BLANK_TAGS = { "mfulc_blank.txt", "mful_blank.txt" };

	public static Type2NdefOperations getType2BlankTag() {
		InMemoryUltralightTag tag = new InMemoryUltralightTag("mful_blank.txt");
		return new Type2NdefOperations(MemoryLayout.ULTRALIGHT, new AcrMfUlReaderWriter(tag), false, true);
	}
}

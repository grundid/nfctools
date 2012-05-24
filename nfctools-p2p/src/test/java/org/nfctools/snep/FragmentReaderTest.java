package org.nfctools.snep;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.utils.NfcUtils;

public class FragmentReaderTest {

	@Test
	public void testIsComplete() throws Exception {

		byte[] f1 = NfcUtils.convertASCIIToBin("100200000011");
		byte[] f2 = NfcUtils.convertASCIIToBin("D1010D55016E");
		byte[] f3 = NfcUtils.convertASCIIToBin("6663746F6F6C");
		byte[] f4 = NfcUtils.convertASCIIToBin("732E6F7267");

		FragmentReader reader = new FragmentReader();
		reader.addFragment(f1);
		assertFalse(reader.isComplete());
		reader.addFragment(f2);
		assertFalse(reader.isComplete());
		reader.addFragment(f3);
		assertFalse(reader.isComplete());
		reader.addFragment(f4);
		assertTrue(reader.isComplete());

	}
}

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

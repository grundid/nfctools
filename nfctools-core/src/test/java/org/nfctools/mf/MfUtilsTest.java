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
package org.nfctools.mf;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.utils.NfcUtils;

public class MfUtilsTest {

	@Test
	public void testNibbleEncoding() throws Exception {
		assertEquals(0x0A, NfcUtils.getMostSignificantNibble((byte)0xAE));
		assertEquals(0x0E, NfcUtils.getLeastSignificantNibble((byte)0xAE));
		assertEquals((byte)0xAE, NfcUtils.encodeNibbles(0x0A, 0x0E));
	}
}

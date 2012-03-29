/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
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
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.mad.MadConstants;

public class MfTrailerBlockTest {

	@Test
	public void testValidAccessConditions() throws Exception {

		assertTrue(TrailerBlock.validAccessConditions(MadConstants.DEFAULT_MAD_ACCESS_CONDITIONS));
		assertTrue(TrailerBlock.validAccessConditions(MfConstants.TRANSPORT_ACCESS_CONDITIONS));
		assertFalse(TrailerBlock.validAccessConditions(new byte[3]));
		assertFalse(TrailerBlock.validAccessConditions(new byte[0]));
	}

	@Test
	public void testKeyBReadable() throws Exception {

		TrailerBlock tr = new TrailerBlock();
		assertTrue(tr.isKeyBReadable());
		tr.setAccessConditions(MadConstants.DEFAULT_MAD_ACCESS_CONDITIONS);
		assertFalse(tr.isKeyBReadable());
		tr.setAccessConditions(MfConstants.NDEF_READ_WRITE_ACCESS_CONDITIONS);
		assertFalse(tr.isKeyBReadable());
	}
}

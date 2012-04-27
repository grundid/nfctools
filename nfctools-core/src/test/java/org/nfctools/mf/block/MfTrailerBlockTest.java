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
package org.nfctools.mf.block;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.mad.MadConstants;

public class MfTrailerBlockTest {

	@Test
	public void testValidAccessConditions() throws Exception {

		assertTrue(TrailerBlock.validAccessConditions(MadConstants.READ_WRITE_ACCESS_CONDITIONS));
		assertTrue(TrailerBlock.validAccessConditions(MfConstants.TRANSPORT_ACCESS_CONDITIONS));
		assertTrue(TrailerBlock.validAccessConditions(MfConstants.NDEF_READ_ONLY_ACCESS_CONDITIONS));
		assertTrue(TrailerBlock.validAccessConditions(MfConstants.NDEF_READ_WRITE_ACCESS_CONDITIONS));
		assertFalse(TrailerBlock.validAccessConditions(new byte[3]));
		assertFalse(TrailerBlock.validAccessConditions(new byte[0]));
	}

	@Test
	public void testKeyBReadable() throws Exception {

		TrailerBlock tr = new TrailerBlock();
		assertTrue(tr.isKeyBReadable());
		tr.setAccessConditions(MadConstants.READ_WRITE_ACCESS_CONDITIONS);
		assertFalse(tr.isKeyBReadable());
		tr.setAccessConditions(MfConstants.NDEF_READ_WRITE_ACCESS_CONDITIONS);
		assertFalse(tr.isKeyBReadable());
	}

	@Test
	public void testCanWriteDataBlock() throws Exception {
		TrailerBlock tr = new TrailerBlock();
		assertTrue(tr.canWriteDataBlock(Key.A, 0));
		assertTrue(tr.canWriteDataBlock(Key.A, 1));
		assertTrue(tr.canWriteDataBlock(Key.A, 2));
		assertTrue(tr.canWriteTrailerBlock(Key.A));
	}

	@Test
	public void testReadOnlyConfig() throws Exception {
		TrailerBlock tr = new TrailerBlock();
		tr.setAccessConditions(MfConstants.NDEF_READ_ONLY_ACCESS_CONDITIONS);
		for (int x = 0; x < 3; x++) {
			assertFalse(tr.canWriteDataBlock(Key.A, x));
			assertFalse(tr.canWriteDataBlock(Key.B, x));
		}
		assertFalse(tr.canWriteTrailerBlock(Key.A));
		assertFalse(tr.canWriteTrailerBlock(Key.B));
	}

	@Test
	public void testReadWriteDataConfig() throws Exception {
		TrailerBlock tr = new TrailerBlock();
		tr.setAccessConditions(MfConstants.NDEF_READ_WRITE_ACCESS_CONDITIONS);
		for (int x = 0; x < 3; x++) {
			assertTrue(tr.canWriteDataBlock(Key.A, x));
			assertTrue(tr.canWriteDataBlock(Key.B, x));
		}
		assertFalse(tr.canWriteTrailerBlock(Key.A));
		assertTrue(tr.canWriteTrailerBlock(Key.B));
	}

	@Test
	public void testReadWriteMadConfig() throws Exception {
		TrailerBlock tr = new TrailerBlock();
		tr.setAccessConditions(MadConstants.READ_WRITE_ACCESS_CONDITIONS);
		for (int x = 0; x < 3; x++) {
			assertFalse(tr.canWriteDataBlock(Key.A, x));
			assertTrue(tr.canWriteDataBlock(Key.B, x));
		}
		assertFalse(tr.canWriteTrailerBlock(Key.A));
		assertTrue(tr.canWriteTrailerBlock(Key.B));
	}
}

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

public class NxpCrcTest {

	@Test
	public void testNxpCrc() throws Exception {

		NxpCrc crc = new NxpCrc();
		crc.add(0x42);

		int save = crc.getCrc();

		crc.reset();

		crc.add(0x42);
		crc.add(save);

		assertEquals(0, crc.getCrc());
	}

	@Test
	public void testMadFromDocs() throws Exception {

		int[] data = { 0x01, 0x01, 0x08, 0x01, 0x08, 0x01, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x03,
				0x10, 0x03, 0x10, 0x02, 0x10, 0x02, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x11, 0x30 };

		NxpCrc crc = new NxpCrc();

		for (int i : data) {
			crc.add(i);
		}

		assertEquals((byte)0x89, crc.getCrc());

	}

	@Test
	public void testMadFromDump1() throws Exception {

		int[] data = { 0x01, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

		NxpCrc crc = new NxpCrc();

		for (int i : data) {
			crc.add(i);
		}

		assertEquals((byte)0xc4, crc.getCrc()); // 0xef

	}

	@Test
	public void testMadFromAndroid() throws Exception {

		int[] data = { 0x01, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03,
				0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1, 0x03, 0xE1 };

		NxpCrc crc = new NxpCrc();

		for (int i : data) {
			crc.add(i);
		}

		assertEquals((byte)0x14, crc.getCrc());
	}
}

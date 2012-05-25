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

public class FragmentIteratorTest {

	@Test
	public void testNext() throws Exception {

		FragmentIterator iterator = new FragmentIterator(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 4);

		assertTrue(iterator.hasNext());

		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, iterator.next());
		assertTrue(iterator.hasNext());
		assertArrayEquals(new byte[] { 5, 6, 7, 8 }, iterator.next());
		assertTrue(iterator.hasNext());
		assertArrayEquals(new byte[] { 9, 10 }, iterator.next());
		assertFalse(iterator.hasNext());

	}
}

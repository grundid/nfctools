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

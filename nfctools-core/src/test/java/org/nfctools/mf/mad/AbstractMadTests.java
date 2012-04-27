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
package org.nfctools.mf.mad;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfException;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.classic.KeyValue;
import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.classic.MfClassicReaderWriter;
import org.nfctools.mf.ul.MemoryMap;
import org.nfctools.spi.acs.AcrMfClassicReaderWriter;
import org.nfctools.test.FileMfClassicReader;
import org.nfctools.test.InMemoryTag;

public abstract class AbstractMadTests {

	private ApplicationId testAppId = new ApplicationId(MfConstants.NDEF_APPLICATION_CODE,
			MfConstants.NDEF_FUNCTION_CLUSTER_CODE);
	private TrailerBlock testNdefTrailerBlock = new TrailerBlock();

	public static final MadKeyConfig MAD_KEY_CONFIG = new MadKeyConfig(Key.A, MfConstants.TRANSPORT_KEY,
			MfConstants.NDEF_KEY);

	public static final KeyValue KEY_VALUE_A = new KeyValue(Key.A, MfConstants.NDEF_KEY);
	public static final KeyValue KEY_VALUE_B = new KeyValue(Key.B, MfConstants.NDEF_KEY);

	protected byte[] dummyKey = MfConstants.NDEF_KEY;
	protected String blankCard;
	protected String cardWithMad;
	protected int maxFreeSpace;
	protected int largeAppSize;
	protected int existingAppSize;
	protected MemoryLayout memoryLayout;

	public AbstractMadTests(String blankCard, String cardWithMad, int maxFreeSpace, int largeAppSize,
			int existingAppSize, MemoryLayout memoryLayout) {
		this.blankCard = blankCard;
		this.cardWithMad = cardWithMad;
		this.maxFreeSpace = maxFreeSpace;
		this.largeAppSize = largeAppSize;
		this.existingAppSize = existingAppSize;
		this.memoryLayout = memoryLayout;
	}

	@Before
	public void setup() {
		try {
			testNdefTrailerBlock.setKey(Key.A, MfConstants.NDEF_KEY);
			testNdefTrailerBlock.setKey(Key.B, MfConstants.NDEF_KEY);
			testNdefTrailerBlock.setGeneralPurposeByte(MfConstants.NDEF_GPB_V10_READ_WRITE);
			testNdefTrailerBlock.setAccessConditions(MfConstants.NDEF_READ_WRITE_ACCESS_CONDITIONS);
		}
		catch (MfException e) {
			e.printStackTrace();
		}
	}

	protected MfClassicReaderWriter loadData(String fileName) {
		try {
			MemoryMap memoryMap = FileMfClassicReader.loadCardFromFile(fileName);
			InMemoryTag tag = new InMemoryTag(memoryMap);

			AcrMfClassicReaderWriter mfClassicReaderWriter = new AcrMfClassicReaderWriter(tag, memoryLayout);
			return mfClassicReaderWriter;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testMadApplicationSizes() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(blankCard);

		ApplicationDirectory applicationDirectory = readerWriter.createApplicationDirectory(MAD_KEY_CONFIG);
		Application application = applicationDirectory.createApplication(testAppId, largeAppSize, dummyKey,
				testNdefTrailerBlock);
		assertArrayEquals(testAppId.getAid(), application.getApplicationId());
		assertEquals(largeAppSize, application.getAllocatedSize());
		byte[] readData = application.read(KEY_VALUE_A);
		assertEquals(largeAppSize, readData.length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMadApplicationTooBig() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(blankCard);
		ApplicationDirectory applicationDirectory = readerWriter.createApplicationDirectory(MAD_KEY_CONFIG);
		applicationDirectory.createApplication(testAppId, maxFreeSpace + 1, dummyKey, testNdefTrailerBlock);
	}

	@Test
	public void testMadOpenReadAid() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(cardWithMad);

		ApplicationDirectory applicationDirectory = readerWriter.getApplicationDirectory();
		Application application = applicationDirectory.openApplication(testAppId);
		assertArrayEquals(testAppId.getAid(), application.getApplicationId());
		assertEquals(existingAppSize, application.getAllocatedSize());
		byte[] readData = application.read(KEY_VALUE_A);
		assertEquals(existingAppSize, readData.length);
	}

	@Test
	public void testMadHasMad() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(cardWithMad);
		assertTrue(readerWriter.hasApplicationDirectory());

		MfClassicReaderWriter readerWriterBlank = loadData(blankCard);
		assertFalse(readerWriterBlank.hasApplicationDirectory());
	}

	@Test
	public void testMadCreate() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(blankCard);

		assertFalse(readerWriter.hasApplicationDirectory());
		readerWriter.createApplicationDirectory(MAD_KEY_CONFIG);
		assertTrue(readerWriter.hasApplicationDirectory());
	}

	@Test
	public void testMadOpenWriteReadApplication() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(cardWithMad);
		ApplicationDirectory applicationDirectory = readerWriter.getApplicationDirectory();
		Application application = applicationDirectory.openApplication(testAppId);

		assertEquals(existingAppSize, application.getAllocatedSize());

		byte[] content = new byte[application.getAllocatedSize()];
		for (int x = 0; x < content.length; x++)
			content[x] = (byte)x;

		application.write(KEY_VALUE_B, content);

		byte[] readContent = application.read(KEY_VALUE_A);
		assertArrayEquals(content, readContent);
	}

	@Test
	public void testMadUpdateApplicationTrailer() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(cardWithMad);

		ApplicationDirectory applicationDirectory = readerWriter.getApplicationDirectory();
		Application application = applicationDirectory.openApplication(testAppId);
		application.updateTrailer(KEY_VALUE_B, testNdefTrailerBlock);

		TrailerBlock readTrailer = application.readTrailer(KEY_VALUE_B);
		assertArrayEquals(testNdefTrailerBlock.getAccessConditions(), readTrailer.getAccessConditions());

	}

	@Test
	public void testMadDeleteApplication() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(cardWithMad);

		ApplicationDirectory applicationDirectory = readerWriter.getApplicationDirectory(MAD_KEY_CONFIG);
		applicationDirectory.deleteApplication(testAppId, dummyKey, testNdefTrailerBlock);

		assertEquals(maxFreeSpace, applicationDirectory.getMaxContinousSize());
	}

	@Test
	public void testMadAllocateSpecifiedMemory() throws Exception {
		MfClassicReaderWriter readerWriter = loadData(blankCard);

		ApplicationDirectory applicationDirectory = readerWriter.createApplicationDirectory(MAD_KEY_CONFIG);
		Application application = applicationDirectory.createApplication(testAppId, 50, dummyKey, testNdefTrailerBlock);
		assertEquals(96, application.getAllocatedSize());
		assertEquals(maxFreeSpace - 96, applicationDirectory.getMaxContinousSize());
	}
}

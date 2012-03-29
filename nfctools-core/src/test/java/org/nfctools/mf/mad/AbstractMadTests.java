package org.nfctools.mf.mad;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.nfctools.mf.Key;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfException;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.spi.file.FileMfReaderWriter;

public abstract class AbstractMadTests {

	private ApplicationId testAppId = new ApplicationId(MfConstants.NDEF_APPLICATION_CODE,
			MfConstants.NDEF_FUNCTION_CLUSTER_CODE);
	private TrailerBlock testNdefTrailerBlock = new TrailerBlock();

	protected byte[] dummyKey = MfConstants.NDEF_KEY;
	protected String emptyCard;
	protected String cardWithMad;
	protected int maxFreeSpace;
	protected int largeAppSize;
	protected int existingAppSize;

	public AbstractMadTests(String emptyCard, String cardWithMad, int maxFreeSpace, int largeAppSize,
			int existingAppSize) {
		this.emptyCard = emptyCard;
		this.cardWithMad = cardWithMad;
		this.maxFreeSpace = maxFreeSpace;
		this.largeAppSize = largeAppSize;
		this.existingAppSize = existingAppSize;
	}

	@Before
	public void setup() {
		try {
			testNdefTrailerBlock.setKey(Key.A, MfConstants.NDEF_KEY);
			testNdefTrailerBlock.setKey(Key.B, MfConstants.NDEF_KEY);
			testNdefTrailerBlock.setGeneralPurposeByte(MfConstants.READ_WRITE_NDEF_GPB);
			testNdefTrailerBlock.setAccessConditions(MfConstants.NDEF_READ_WRITE_ACCESS_CONDITIONS);
		}
		catch (MfException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMadApplicationSizes() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(emptyCard);
		ApplicationDirectory applicationDirectory = MadUtils.createApplicationDirectory(mfCard, mfReaderWriter, Key.A,
				MfConstants.TRANSPORT_KEY, dummyKey);
		Application application = applicationDirectory.createApplication(testAppId, largeAppSize, dummyKey,
				testNdefTrailerBlock);
		assertArrayEquals(testAppId.getAid(), application.getApplicationId());
		assertEquals(largeAppSize, application.getAllocatedSize());
		byte[] readData = application.read(Key.A, dummyKey);
		assertEquals(largeAppSize, readData.length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMadApplicationTooBig() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(emptyCard);
		ApplicationDirectory applicationDirectory = MadUtils.createApplicationDirectory(mfCard, mfReaderWriter, Key.A,
				MfConstants.TRANSPORT_KEY, dummyKey);
		applicationDirectory.createApplication(testAppId, maxFreeSpace + 1, dummyKey, testNdefTrailerBlock);
	}

	@Test
	public void testMadOpenReadAid() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(cardWithMad);
		ApplicationDirectory applicationDirectory = MadUtils.getApplicationDirectory(mfCard, mfReaderWriter);
		Application application = applicationDirectory.openApplication(testAppId);
		assertArrayEquals(testAppId.getAid(), application.getApplicationId());
		assertEquals(existingAppSize, application.getAllocatedSize());
		byte[] readData = application.read(Key.A, dummyKey);
		assertEquals(existingAppSize, readData.length);
	}

	@Test
	public void testMadHasMad() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(cardWithMad);
		assertTrue(MadUtils.hasApplicationDirectory(mfCard, mfReaderWriter));

		MfCard mfCardEmpty = mfReaderWriter.loadCardFromFile(emptyCard);
		assertFalse(MadUtils.hasApplicationDirectory(mfCardEmpty, mfReaderWriter));
	}

	@Test
	public void testMadCreate() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCardEmpty = mfReaderWriter.loadCardFromFile(emptyCard);
		assertFalse(MadUtils.hasApplicationDirectory(mfCardEmpty, mfReaderWriter));
		MadUtils.createApplicationDirectory(mfCardEmpty, mfReaderWriter, Key.A, MfConstants.TRANSPORT_KEY, dummyKey);
		assertTrue(MadUtils.hasApplicationDirectory(mfCardEmpty, mfReaderWriter));
	}

	@Test
	public void testMadOpenWriteReadApplication() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(cardWithMad);
		ApplicationDirectory applicationDirectory = MadUtils.getApplicationDirectory(mfCard, mfReaderWriter);
		Application application = applicationDirectory.openApplication(testAppId);

		assertEquals(existingAppSize, application.getAllocatedSize());

		byte[] content = new byte[application.getAllocatedSize()];
		for (int x = 0; x < content.length; x++)
			content[x] = (byte)x;

		application.write(Key.B, dummyKey, content);

		byte[] readContent = application.read(Key.A, dummyKey);
		assertArrayEquals(content, readContent);
	}

	@Test
	public void testMadUpdateApplicationTrailer() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(cardWithMad);
		ApplicationDirectory applicationDirectory = MadUtils.getApplicationDirectory(mfCard, mfReaderWriter);
		Application application = applicationDirectory.openApplication(testAppId);
		application.updateTrailer(Key.B, dummyKey, testNdefTrailerBlock);
	}

	@Test
	public void testMadDeleteApplication() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(cardWithMad);
		ApplicationDirectory applicationDirectory = MadUtils.getApplicationDirectory(mfCard, mfReaderWriter, dummyKey);
		applicationDirectory.deleteApplication(testAppId, dummyKey, testNdefTrailerBlock);

		assertEquals(maxFreeSpace, applicationDirectory.getMaxContinousSize());
	}

	@Test
	public void testMadAllocateSpecifiedMemory() throws Exception {

		FileMfReaderWriter mfReaderWriter = new FileMfReaderWriter();
		MfCard mfCard = mfReaderWriter.loadCardFromFile(emptyCard);
		ApplicationDirectory applicationDirectory = MadUtils.createApplicationDirectory(mfCard, mfReaderWriter, Key.A,
				MfConstants.TRANSPORT_KEY, dummyKey);
		Application application = applicationDirectory.createApplication(testAppId, 50, dummyKey, testNdefTrailerBlock);
		assertEquals(96, application.getAllocatedSize());
		assertEquals(maxFreeSpace - 96, applicationDirectory.getMaxContinousSize());
	}

}

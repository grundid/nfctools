package org.nfctools.spi.acs;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.api.TagType;
import org.nfctools.utils.NfcUtils;

public class AcsTagUtilsTest {

	@Test
	public void testIdentifyTagType() throws Exception {
		assertEquals(TagType.NFCIP,
				AcsTagUtils.identifyTagType(NfcUtils.convertASCIIToBin("804F0CA00000030603FF4000000000")));
	}
}

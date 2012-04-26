package org.nfctools.ndef.decoder;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.Action;
import org.nfctools.ndef.wkt.records.SmartPosterRecord;
import org.nfctools.utils.NfcUtils;

public class SmartPosterDecoderTest {

	public static final String smartPoster = "D10227537091010B540564652D44455469746C6511010D550177696E667574757"
			+ "2652E646551030161637400";

	@Test
	public void testDecode() throws Exception {
		Record record = NdefContext.getNdefMessageDecoder().decodeToRecord(NfcUtils.convertASCIIToBin(smartPoster));
		assertTrue(record instanceof SmartPosterRecord);
		SmartPosterRecord smartPosterRecord = (SmartPosterRecord)record;

		assertEquals("Title", smartPosterRecord.getTitle().getText());
		assertEquals(Action.DEFAULT_ACTION, smartPosterRecord.getAction().getAction());
		assertEquals("http://www.winfuture.de", smartPosterRecord.getUri().getUri());

	}
}

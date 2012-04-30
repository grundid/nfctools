package org.nfctools.llcp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.test.NfcipHelper;
import org.nfctools.test.NotifyingNdefListener;

public class LlcpTest {

	@Test
	public void testSendReceiveNdefOverNPP() throws Exception {
		NotifyingNdefListener ndefListener = new NotifyingNdefListener(this);
		NfcipHelper helper = new NfcipHelper();
		helper.registerNPPOnInitiator(ndefListener);
		List<Record> records = new ArrayList<Record>();
		records.add(new UriRecord("http://www.nfctools.org"));

		helper.registerNPPOnTarget().addMessages(records, null);
		helper.launch();

		synchronized (this) {
			wait(5000);
		}
		assertFalse(ndefListener.getRecords().isEmpty());
		UriRecord uriRecord = (UriRecord)ndefListener.getRecords().iterator().next();
		assertEquals("http://www.nfctools.org", uriRecord.getUri());
	}
}

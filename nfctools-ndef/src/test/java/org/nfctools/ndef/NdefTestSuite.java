package org.nfctools.ndef;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.nfctools.ndef.decoder.GenericControlRecordDecoderTest;
import org.nfctools.ndef.decoder.HandoverDecoderTest;
import org.nfctools.ndef.decoder.UriRecordDecoderTest;
import org.nfctools.ndef.encoder.GenericControlRecordEncoderTest;
import org.nfctools.ndef.encoder.SmartPosterRecordEncoderTest;
import org.nfctools.ndef.encoder.TextRecordEncoderTest;
import org.nfctools.ndef.encoder.UriRecordEncoderTest;
import org.nfctools.ndef.records.TextRecordTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	NdefEncodeDecodeRoundtripTest.class, NdefMessageDecoderTest.class, NdefMessageEncoderTest.class, NdefRecordDecoderTest.class, // base package
	GenericControlRecordDecoderTest.class, HandoverDecoderTest.class, UriRecordDecoderTest.class, // decoder package
	GenericControlRecordEncoderTest.class, SmartPosterRecordEncoderTest.class, TextRecordEncoderTest.class, UriRecordEncoderTest.class, // encoder package
	TextRecordTest.class // records package
})

public class NdefTestSuite {
	// empty
}
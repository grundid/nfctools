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
package org.nfctools.mf.ndef;

import java.io.IOException;
import java.util.List;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.classic.KeyValue;
import org.nfctools.mf.mad.Application;
import org.nfctools.mf.mad.ApplicationDirectory;
import org.nfctools.mf.mad.MadUtils;
import org.nfctools.mf.tlv.NdefMessageTlv;
import org.nfctools.mf.tlv.TypeLengthValueWriter;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefWriter;
import org.nfctools.ndef.Record;
import org.nfctools.tags.TagOutputStream;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class MfNdefWriter implements NdefWriter<MfCard> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MfReaderWriter readerWriter;
	private NdefMessageEncoder ndefMessageEncoder;
	private Key createKey = Key.A;
	private byte[] createKeyValue = MfConstants.TRANSPORT_KEY;
	private byte[] writeKeyValue = MfConstants.NDEF_KEY;
	private byte[] deleteKeyValue = MfConstants.NDEF_KEY;

	public MfNdefWriter(MfReaderWriter readerWriter, NdefMessageEncoder ndefMessageEncoder) {
		this.readerWriter = readerWriter;
		this.ndefMessageEncoder = ndefMessageEncoder;
	}

	/**
	 * Sets the create key for the MAD sectors. If a mifare card does not have a MAD in sector 0 it is assumed that it
	 * is in the transport configuration. In this case the writer will use the create key to write the MAD trailer.
	 * Default is MfConstants.TRANSPORT_KEY.
	 * 
	 * @param createKey
	 */
	public void setCreateKey(Key createKey) {
		this.createKey = createKey;
	}

	/**
	 * Sets the write key to protect the NDEF sectors. This is your secret key. The default is MfConstants.NDEF_KEY.
	 * 
	 * @param writeKeyValue
	 */
	public void setWriteKeyValue(byte[] writeKeyValue) {
		this.writeKeyValue = writeKeyValue;
	}

	/**
	 * Sets the delete key to be able to delete any previous NDEF sectors. The default is MfConstants.NDEF_KEY.
	 * 
	 * @param deleteKeyValue
	 */
	public void setDeleteKeyValue(byte[] deleteKeyValue) {
		this.deleteKeyValue = deleteKeyValue;
	}

	@Override
	public void writeNdefMessage(MfCard card, List<Record> records) throws IOException {

		byte[] ndefData = ndefMessageEncoder.encode(records);

		writeNdefMessage(card, ndefData);
	}

	private void writeNdefMessage(MfCard card, byte[] ndefData) throws IOException, MfException {
		ApplicationDirectory applicationDirectory = null;

		if (MadUtils.hasApplicationDirectory(card, readerWriter))
			applicationDirectory = MadUtils.getApplicationDirectory(card, readerWriter, writeKeyValue);
		else
			applicationDirectory = MadUtils.createApplicationDirectory(card, readerWriter, createKey, createKeyValue,
					writeKeyValue);

		TrailerBlock trailerBlock = createNdefTrailerBlock();

		if (applicationDirectory.hasApplication(MfNdefConstants.NDEF_APP_ID)) {
			applicationDirectory.deleteApplication(MfNdefConstants.NDEF_APP_ID, deleteKeyValue, new TrailerBlock());
		}

		byte[] tlvWrappedNdefData = wrapNdefMessageWithTlv(ndefData, applicationDirectory.getMaxContinousSize());

		/*
		 * The specs states that the TLV terminator can be left out if the message ends with the available space.
		 * One might want to check if the available size on the card is one byte less than the tlv wrapped message. If 
		 * it is the case the TLV terminator could be removed.
		 */
		Application application = applicationDirectory.createApplication(MfNdefConstants.NDEF_APP_ID,
				tlvWrappedNdefData.length, writeKeyValue, trailerBlock);

		if (log.isDebugEnabled())
			log.debug("Length: " + tlvWrappedNdefData.length + " [" + NfcUtils.convertBinToASCII(tlvWrappedNdefData)
					+ "]");

		application.write(new KeyValue(Key.B, writeKeyValue), tlvWrappedNdefData);
	}

	private TrailerBlock createNdefTrailerBlock() throws MfException {
		TrailerBlock trailerBlock = new TrailerBlock();
		trailerBlock.setKey(Key.A, MfConstants.NDEF_KEY);
		trailerBlock.setKey(Key.B, writeKeyValue);
		trailerBlock.setAccessConditions(MfConstants.NDEF_READ_WRITE_ACCESS_CONDITIONS);
		trailerBlock.setGeneralPurposeByte(MfConstants.NDEF_GPB_V10_READ_WRITE);
		return trailerBlock;
	}

	private byte[] wrapNdefMessageWithTlv(byte[] ndefMessage, int maxSize) {
		TagOutputStream out = new TagOutputStream(maxSize);
		TypeLengthValueWriter writer = new TypeLengthValueWriter(out);
		writer.write(new NdefMessageTlv(ndefMessage));
		writer.close();

		return out.getBuffer();
	}
}

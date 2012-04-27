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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.classic.KeyValue;
import org.nfctools.mf.mad.Application;
import org.nfctools.mf.mad.ApplicationDirectory;
import org.nfctools.mf.mad.MadUtils;
import org.nfctools.mf.tlv.NdefMessageTlv;
import org.nfctools.mf.tlv.Tlv;
import org.nfctools.mf.tlv.TypeLengthValueReader;
import org.nfctools.ndef.NdefException;
import org.nfctools.ndef.NdefMessage;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefReader;
import org.nfctools.ndef.Record;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class MfNdefReader implements NdefReader<MfCard> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MfReaderWriter readerWriter;
	private NdefMessageDecoder ndefMessageDecoder;

	public MfNdefReader(MfReaderWriter readerWriter, NdefMessageDecoder ndefMessageDecoder) {
		this.readerWriter = readerWriter;
		this.ndefMessageDecoder = ndefMessageDecoder;
	}

	@Override
	public List<Record> readNdefMessage(MfCard card) throws IOException {

		if (!MadUtils.hasApplicationDirectory(card, readerWriter))
			throw new NdefException("unknown service type");

		ApplicationDirectory applicationDirectory = MadUtils.getApplicationDirectory(card, readerWriter);

		if (applicationDirectory.hasApplication(MfNdefConstants.NDEF_APP_ID)) {

			Application application = applicationDirectory.openApplication(MfNdefConstants.NDEF_APP_ID);

			byte[] tlvWrappedNdefMessage = application.read(new KeyValue(Key.A, MfConstants.NDEF_KEY));

			if (log.isDebugEnabled())
				log.debug(NfcUtils.convertBinToASCII(tlvWrappedNdefMessage));

			TypeLengthValueReader lengthValueReader = new TypeLengthValueReader(new ByteArrayInputStream(
					tlvWrappedNdefMessage));

			List<Record> records = new ArrayList<Record>();

			while (lengthValueReader.hasNext()) {
				Tlv tlv = lengthValueReader.next();
				if (tlv instanceof NdefMessageTlv) {
					NdefMessage ndefMessage = ndefMessageDecoder.decode(((NdefMessageTlv)tlv).getNdefMessage());
					for (Record record : ndefMessageDecoder.decodeToRecords(ndefMessage)) {
						records.add(record);
					}
				}
			}
			return records;
		}
		else {
			throw new NdefException("unknown service type");
		}
	}
}

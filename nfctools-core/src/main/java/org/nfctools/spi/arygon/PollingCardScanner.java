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
package org.nfctools.spi.arygon;

import java.io.IOException;

import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollingCardScanner implements Runnable {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ArygonHighLevelReaderWriter nfcReaderWriter;
	private MfCardListener cardListener;
	private MfReaderWriter readerWriter;

	public PollingCardScanner(ArygonHighLevelReaderWriter nfcReaderWriter, MfCardListener cardListener,
			MfReaderWriter readerWriter) {
		this.nfcReaderWriter = nfcReaderWriter;
		this.cardListener = cardListener;
		this.readerWriter = readerWriter;
	}

	@Override
	public void run() {

		log.debug("Polling started");
		MfCard card = null;
		while (!Thread.interrupted()) {
			try {
				if (nfcReaderWriter.hasData()) {
					log.debug("Reader has data");
					try {
						card = ((ArygonReaderWriter)readerWriter).readCard();
						cardListener.cardDetected(card, readerWriter);
						readerWriter.setCardIntoHalt(card);
					}
					catch (IOException e) {
						try {
							log.trace("Halting card. " + e.getMessage(), e);
							readerWriter.reselectCard(card);
							readerWriter.setCardIntoHalt(card);
						}
						catch (Exception e1) {
							log.trace("Halting failed...");
							Thread.sleep(1000);
						}
					}
					((ArygonReaderWriter)readerWriter).scanForCard();
				}
				else {
					Thread.sleep(10);
				}
			}
			catch (IOException e) {
				log.error(e.getMessage(), e);
				break;
			}
			catch (InterruptedException e) {
				return;
			}
		}
		log.debug("DONE " + Thread.currentThread().getName());
	}
}

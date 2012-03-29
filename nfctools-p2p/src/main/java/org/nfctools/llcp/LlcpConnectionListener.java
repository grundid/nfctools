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
package org.nfctools.llcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nfctools.llcp.parameter.Miux;
import org.nfctools.llcp.parameter.ServiceName;
import org.nfctools.llcp.pdu.AbstractProtocolDataUnit;
import org.nfctools.llcp.pdu.Connect;
import org.nfctools.llcp.pdu.ConnectComplete;
import org.nfctools.llcp.pdu.Disconnect;
import org.nfctools.llcp.pdu.DisconnectedMode;
import org.nfctools.llcp.pdu.Information;
import org.nfctools.llcp.pdu.PduDecoder;
import org.nfctools.llcp.pdu.ReceiveReady;
import org.nfctools.llcp.pdu.Symmetry;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefListener;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.ndefpush.NdefPushProtocol;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPConnectionListener;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LlcpConnectionListener implements NFCIPConnectionListener {

	private Logger log = LoggerFactory.getLogger(getClass());
	private PduDecoder pduDecoder = new PduDecoder();

	//	private Record ndefMessageToSend =
	//
	//	new TextMimeRecord(
	//			"text/x-vcard",
	//
	//			"BEGIN:VCARD\n"
	//					+ "VERSION:2.1\n"
	//					+ "N:Gump;Forrest\n"
	//					+ "FN:Forrest Gump\n"
	//					+ "ORG:Bubba Gump Shrimp Co.\n"
	//					+ "TITLE:Shrimp Man\n"
	//					+ "TEL;WORK;VOICE:(111) 555-1212\n"
	//					+ "TEL;HOME;VOICE:(404) 555-1212\n"
	//					+ "ADR;WORK:;;100 Waters Edge;Baytown;LA;30314;United States of America\n"
	//					+ "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:100 Waters Edge=0D=0ABaytown, LA 30314=0D=0AUnited States of America\n"
	//					+ "ADR;HOME:;;42 Plantation St.;Baytown;LA;30314;United States of America\n"
	//					+ "LABEL;HOME;ENCODING=QUOTED-PRINTABLE:42 Plantation St.=0D=0ABaytown, LA 30314=0D=0AUnited States of America\n"
	//					+ "EMAIL;PREF;INTERNET:forrestgump@example.com\n" + "REV:20080424T195243Z\n" + "END:VCARD");

	private Record ndefMessageToSend = new UriRecord(
			"http://www.grundid-gmbh.de/mobile?test1=very_long_url&test1=very_long_url&test1=very_long_url&test1=very_long_url&test1=very_long_url");

	//	private Record ndefMessageToSend = new UriRecord("tel:+49626945029?dial=true");
	//	private static Record ndefMessageToSend = new SmartPosterRecord();
	//	static {
	//		((SmartPosterRecord)ndefMessageToSend).setAction(new ActionRecord(Action.OPEN_FOR_EDITING));
	//		((SmartPosterRecord)ndefMessageToSend).setUri(new UriRecord("tel:+49626945029"));
	//	}

	private NdefListener ndefListener;

	@Override
	public void onConnection(NFCIPConnection connection) throws IOException {
		log.info("connection, wait for robot...");
		//		sleep(10); // was 100ms
		boolean connected = true;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ndefMessageToSend = null;
		if (connection.isInitiator()) {
			System.out.println("Send Symmetry...");
			connection.send(pduDecoder.encode(new Symmetry()));
		}

		while (connected) {
			byte[] data = connection.receive();
			long time = System.nanoTime();
			log.debug("Received: " + data.length + " | " + NfcUtils.convertBinToASCII(data));
			AbstractProtocolDataUnit pduToSend = null;

			AbstractProtocolDataUnit protocolDataUnit = pduDecoder.decode(data);
			log.debug("decoded:" + protocolDataUnit.toString());
			if (protocolDataUnit instanceof Connect) {
				Connect connect = (Connect)protocolDataUnit;
				if (connect.getDestinationServiceAccessPoint() == 4) {
					pduToSend = new DisconnectedMode(connect.getSourceServiceAccessPoint(),
							connect.getDestinationServiceAccessPoint(), 2);
				}
				else {
					List<Object> parameter = new ArrayList<Object>();
					parameter.add(new Miux(120));
					pduToSend = new ConnectComplete(connect.getSourceServiceAccessPoint(),
							connect.getDestinationServiceAccessPoint(), parameter);
				}

			}
			else if (protocolDataUnit instanceof Information) {
				Information information = (Information)protocolDataUnit;

				log.warn("Information: Send: " + information.getSend() + " Rec: " + information.getReceived());

				List<byte[]> ndefMessages = null;
				baos.write(information.getServiceDataUnit());
				try {
					ndefMessages = NdefPushProtocol.parse(baos.toByteArray());
					baos.reset();
				}
				catch (Exception e) {
				}

				if (ndefMessages != null) {
					for (byte[] ndef : ndefMessages) {
						List<Record> records = NdefContext.getNdefMessageDecoder().decodeToRecords(ndef);
						for (Record record : records) {
							log.warn(record.toString());
						}
						if (ndefListener != null) {
							ndefListener.onNdefMessages(records);
						}
					}
				}

				pduToSend = new ReceiveReady(information.getSourceServiceAccessPoint(),
						information.getDestinationServiceAccessPoint(), information.getSend() + 1);

			}
			else if (protocolDataUnit instanceof Symmetry) {

				log.debug("" + ndefMessageToSend);
				if (ndefMessageToSend == null) {
					try {
						Thread.sleep(10);
					}
					catch (InterruptedException e) {
					}
					pduToSend = new Symmetry();
				}
				else {
					List<Object> parameter = new ArrayList<Object>();
					parameter.add(new Miux(120));
					parameter.add(new ServiceName("com.android.npp"));
					pduToSend = new Connect(1, 0x32, parameter);
				}
			}
			else if (protocolDataUnit instanceof Disconnect) {
				pduToSend = new DisconnectedMode(protocolDataUnit.getSourceServiceAccessPoint(),
						protocolDataUnit.getDestinationServiceAccessPoint(), 0);
				//				connected = false;
			}
			else if (protocolDataUnit instanceof ConnectComplete) {

				byte[] serviceDataUnit = NdefPushProtocol.toByteArray(Arrays.asList(ndefMessageToSend));

				pduToSend = new Information(protocolDataUnit.getSourceServiceAccessPoint(),
						protocolDataUnit.getDestinationServiceAccessPoint(), 0, 0, serviceDataUnit);
				ndefMessageToSend = null;
			}
			else if (protocolDataUnit instanceof ReceiveReady) {
				pduToSend = new Disconnect(protocolDataUnit.getSourceServiceAccessPoint(),
						protocolDataUnit.getDestinationServiceAccessPoint());
				ndefMessageToSend = null;
			}
			else if (protocolDataUnit instanceof DisconnectedMode) {
				pduToSend = new Symmetry();
			}
			else {
				// TODO cannot handle packet
				System.out.println("UNKNOWN PDU:" + protocolDataUnit);
				pduToSend = new Symmetry();
			}

			if (pduToSend != null) {
				log.debug("" + pduToSend);
				byte[] pdu = pduDecoder.encode(pduToSend);
				log.debug("sending PDU: " + NfcUtils.convertBinToASCII(pdu) + " time: "
						+ ((System.nanoTime() - time) / 1000000) + " ms");
				connection.send(pdu);
			}

		}
		log.info("DONE");

	}

	public void setNdefListener(NdefListener ndefListener) {
		this.ndefListener = ndefListener;
	}

	private void sleep(long millis) {
		try {
			log.debug("Sleeping " + millis);
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

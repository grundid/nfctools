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
package org.nfctools.spi.tama.nfcip;

import java.io.IOException;

import org.nfctools.NfcContext;
import org.nfctools.api.ConnectionSetup;
import org.nfctools.io.ByteArrayReader;
import org.nfctools.io.ByteArrayWriter;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.nfcip.NFCIPConnectionListener;
import org.nfctools.nfcip.NFCIPManager;
import org.nfctools.spi.tama.AbstractTamaCommunicator;
import org.nfctools.spi.tama.TamaConstants;
import org.nfctools.spi.tama.request.InitTamaTargetReq;
import org.nfctools.spi.tama.request.JumpForDepReq;
import org.nfctools.spi.tama.request.ReleaseReq;
import org.nfctools.spi.tama.request.RfCommunicationReq;
import org.nfctools.spi.tama.response.InitTamaTargetResp;
import org.nfctools.spi.tama.response.JumpForDepResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TamaNfcIpCommunicator extends AbstractTamaCommunicator implements NFCIPManager {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private byte[] felicaParams = new byte[TamaConstants.FELICA_PARAM_LENGTH];
	private byte[] mifareParams = new byte[TamaConstants.MIFARE_PARAM_LENGTH];
	private byte[] nfcId = new byte[TamaConstants.NFCID_PARAM_LENGTH];

	private boolean activeInitiator = true;
	private byte baudRateInitiator = TamaConstants.BAUD_RATE_424;

	private boolean depOnlyTarget = false;
	private boolean passiveOnlyTarget = false;

	private byte[] generalBytes;

	private NFCIPConnectionListener connectionListener;
	//	private TargetListener targetListener;

	private Thread waitingThread;

	public TamaNfcIpCommunicator(ByteArrayReader reader, ByteArrayWriter writer) {
		super(reader, writer);
	}

	public void setDepOnlyTarget(boolean depOnlyTarget) {
		this.depOnlyTarget = depOnlyTarget;
	}

	public void setPassiveOnlyTarget(boolean passiveOnlyTarget) {
		this.passiveOnlyTarget = passiveOnlyTarget;
	}

	public void setFelicaParams(byte[] felicaParams) {
		this.felicaParams = felicaParams;
	}

	public void setMifareParams(byte[] mifareParams) {
		this.mifareParams = mifareParams;
	}

	public void setNfcId(byte[] nfcId) {
		this.nfcId = nfcId;
	}

	public void setGeneralBytes(byte[] generalBytes) {
		this.generalBytes = generalBytes;
	}

	public void setConnectionSetup(ConnectionSetup setup) {
		setFelicaParams(setup.felicaParams);
		setMifareParams(setup.mifareParams);
		setNfcId(setup.nfcId3t);
		setGeneralBytes(setup.generalBytes);
	}

	@Override
	public NFCIPConnection connectAsInitiator() throws IOException {
		byte[] passiveInitiatorData = { 0x00, (byte)0xff, (byte)0xff, 0x00, 0x00 };
		JumpForDepResp jumpForDepResp = sendMessage(new JumpForDepReq(activeInitiator, baudRateInitiator,
				passiveInitiatorData, nfcId, generalBytes));

		return new InitiatorNfcIpConnection(this, jumpForDepResp.getGeneralBytes(), jumpForDepResp.getTargetId());
	}

	public void releaseTargets() throws IOException {
		sendMessage(new ReleaseReq(0));
	}

	@Override
	public NFCIPConnection connectAsTarget() throws IOException {
		InitTamaTargetResp initTamaTargetResp = sendMessage(new InitTamaTargetReq(depOnlyTarget, passiveOnlyTarget,
				mifareParams, felicaParams, nfcId, generalBytes));

		// FIXME Initiator Command is not really the nfcid. Must investigate. 
		// FIXME Extract general Bytes
		byte[] generalBytes = initTamaTargetResp.getInitiatorCommand();

		return new TargetNfcIpConnection(this, generalBytes);
	}

	//	@Override
	//	public void setTargetListener(TargetListener targetListener) throws IOException {
	//		sendMessage(new SetParametersReq().setAutomaticATR_RES(false));
	//		this.targetListener = targetListener;
	//	}

	@Override
	public void setConnectionListener(NFCIPConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	public void setTimeout(long millis) {
		reader.setTimeout(millis);
	}

	private void initRfTimings() throws IOException {
		byte[] configData = { 0x00, 0x0d, 0x0c };
		sendMessage(new RfCommunicationReq(0x02, configData));
	}

	private void initRfRetries() throws IOException {
		byte[] configData = { (byte)0xff, (byte)0xff, 0x00 };
		sendMessage(new RfCommunicationReq(0x05, configData));
	}

	@Override
	public void initAsTarget() throws IOException {
		//		initRfTimings();
		//		initRfRetries();
		waitingThread = new Thread(new Runnable() {

			@Override
			public void run() {

				while (!Thread.interrupted()) {
					try {
						setTimeout(-1);
						InitTamaTargetResp initTamaTargetResp = sendMessage(new InitTamaTargetReq(depOnlyTarget,
								passiveOnlyTarget, mifareParams, felicaParams, nfcId, generalBytes));
						NfcContext nfcContext = new NfcContext();
						nfcContext.setAttribute(NfcContext.KEY_COMMUNICATOR, TamaNfcIpCommunicator.this);

						byte[] generalBytes = initTamaTargetResp.getInitiatorCommand();
						//						if (targetListener != null) {
						//														targetListener.onTarget(target, nfcContext);
						//						}
						if (connectionListener != null) {
							connectionListener.onConnection(new TargetNfcIpConnection(TamaNfcIpCommunicator.this,
									generalBytes));
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						break;
					}
				}

				log.info("THREAD DONE");
			}
		});
		waitingThread.start();
	}

	public void close() {
		if (waitingThread != null && waitingThread.isAlive())
			waitingThread.interrupt();
	}
}

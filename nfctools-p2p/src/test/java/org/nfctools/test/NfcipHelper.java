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
package org.nfctools.test;

import java.io.IOException;

import org.nfctools.llcp.LlcpConnectionManager;
import org.nfctools.llcp.LlcpConnectionManagerFactory;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.llcp.LlcpOverNfcip;
import org.nfctools.ndef.NdefListener;
import org.nfctools.ndefpush.NdefPushLlcpService;
import org.nfctools.snep.SnepClient;
import org.nfctools.snep.SnepConstants;
import org.nfctools.snep.SnepServer;

public class NfcipHelper {

	private Thread threadTarget;
	private Thread threadInitiator;
	private LlcpOverNfcip initiatorLlcp;
	private LlcpOverNfcip targetLlcp;

	public NfcipHelper(final SnepServer snepServer, final SnepClient snepClient,
			final NdefListener initiatorNdefListener, final NdefPushLlcpService targetNdefPushLlcpService) {
		final NfcipConnectionDummy initiator = new NfcipConnectionDummy(true, LlcpConstants.initiatorGeneralBytes);
		final NfcipConnectionDummy target = new NfcipConnectionDummy(false, LlcpConstants.initiatorGeneralBytes);
		initiator.setOther(target);
		target.setOther(initiator);
		initiatorLlcp = new LlcpOverNfcip(new LlcpConnectionManagerFactory() {

			@Override
			protected void configureConnectionManager(LlcpConnectionManager connectionManager) {
				if (snepServer != null) {
					connectionManager.registerWellKnownServiceAccessPoint(SnepConstants.SNEP_SERVICE_NAME, snepServer);
					connectionManager.registerServiceAccessPoint(SnepConstants.SNEP_SERVICE_ADDRESS, snepServer);
				}
				if (initiatorNdefListener != null) {
					NdefPushLlcpService ndefPushLlcpService = new NdefPushLlcpService(initiatorNdefListener);
					connectionManager.registerWellKnownServiceAccessPoint(LlcpConstants.COM_ANDROID_NPP,
							ndefPushLlcpService);
				}
			}
		});
		targetLlcp = new LlcpOverNfcip(new LlcpConnectionManagerFactory() {

			@Override
			protected void configureConnectionManager(LlcpConnectionManager connectionManager) {
				if (snepClient != null)
					connectionManager.registerServiceAccessPoint(snepClient);
				if (targetNdefPushLlcpService != null)
					connectionManager.registerWellKnownServiceAccessPoint(LlcpConstants.COM_ANDROID_NPP,
							targetNdefPushLlcpService);
			}
		});
		threadInitiator = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					initiatorLlcp.onConnection(initiator);
				}
				catch (IOException e) {
				}
			}
		});
		threadTarget = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					targetLlcp.onConnection(target);
				}
				catch (IOException e) {
				}
			}
		});
	}

	public void launch() {
		threadTarget.start();
		threadInitiator.start();
	}

	public LlcpOverNfcip getInitiatorLlcp() {
		return initiatorLlcp;
	}

	public LlcpOverNfcip getTargetLlcp() {
		return targetLlcp;
	}
	//	public NdefPushLlcpService registerNPPOnInitiator() {
	//		return setupNpp(initiatorLlcp, new LoggingNdefListener());
	//	}
	//
	//	public void registerNPPOnInitiator(NdefListener ndefListener) {
	//		setupNpp(initiatorLlcp, ndefListener);
	//	}
	//
	//	public NdefPushLlcpService registerNPPOnTarget() {
	//		return setupNpp(targetLlcp, new LoggingNdefListener());
	//	}
}

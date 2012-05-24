package org.nfctools.test;

import java.io.IOException;

import org.nfctools.llcp.LlcpConnectionManager;
import org.nfctools.llcp.LlcpConstants;
import org.nfctools.llcp.LlcpOverNfcip;
import org.nfctools.ndef.NdefListener;
import org.nfctools.ndefpush.NdefPushLlcpService;
import org.nfctools.snep.SnepClient;
import org.nfctools.snep.SnepConstants;
import org.nfctools.snep.SnepServer;
import org.nfctools.utils.LoggingNdefListener;

public class NfcipHelper {

	private Thread threadTarget;
	private Thread threadInitiator;
	private LlcpOverNfcip initiatorLlcp;
	private LlcpOverNfcip targetLlcp;

	public NfcipHelper() {
		final NfcipConnectionDummy initiator = new NfcipConnectionDummy(true, LlcpConstants.initiatorGeneralBytes);
		final NfcipConnectionDummy target = new NfcipConnectionDummy(false, LlcpConstants.initiatorGeneralBytes);
		initiator.setOther(target);
		target.setOther(initiator);

		initiatorLlcp = new LlcpOverNfcip();
		targetLlcp = new LlcpOverNfcip();

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

	public NdefPushLlcpService registerNPPOnInitiator() {
		return setupNpp(initiatorLlcp, new LoggingNdefListener());
	}

	public NdefPushLlcpService registerNPPOnInitiator(NdefListener ndefListener) {
		return setupNpp(initiatorLlcp, ndefListener);
	}

	public NdefPushLlcpService registerNPPOnTarget() {
		return setupNpp(targetLlcp, new LoggingNdefListener());
	}

	public void registerSnepServerInitiator(SnepServer snepServer) {
		LlcpConnectionManager connectionManager = initiatorLlcp.getConnectionManager();
		connectionManager.registerWellKnownServiceAccessPoint(SnepConstants.SNEP_SERVICE_NAME, snepServer);
		connectionManager.registerServiceAccessPoint(SnepConstants.SNEP_SERVICE_ADDRESS, snepServer);
	}

	public void registerSnepClientTarget(SnepClient snepClient) {
		LlcpConnectionManager connectionManager = targetLlcp.getConnectionManager();
		connectionManager.registerServiceAccessPoint(snepClient);
	}

	private NdefPushLlcpService setupNpp(LlcpOverNfcip llcpOverNfcip, NdefListener ndefListener) {
		NdefPushLlcpService ndefPushLlcpService = new NdefPushLlcpService(ndefListener);
		LlcpConnectionManager connectionManager = llcpOverNfcip.getConnectionManager();
		connectionManager.registerWellKnownServiceAccessPoint(LlcpConstants.COM_ANDROID_NPP, ndefPushLlcpService);
		return ndefPushLlcpService;
	}

}

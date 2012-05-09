package org.nfctools.test;

import java.io.IOException;

import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NfcipConnectionDummy implements NFCIPConnection {

	private Logger log = LoggerFactory.getLogger(getClass());

	private boolean initiator;
	private byte[] generalBytes;
	private NfcipConnectionDummy other;
	private byte[] buffer;

	public NfcipConnectionDummy(boolean initiator, byte[] generalBytes) {
		this.initiator = initiator;
		this.generalBytes = generalBytes;
	}

	public void setOther(NfcipConnectionDummy other) {
		this.other = other;
	}

	private void onMessage(byte[] data) {
		buffer = data;
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public boolean isTarget() {
		return !initiator;
	}

	@Override
	public boolean isInitiator() {
		return initiator;
	}

	@Override
	public byte[] receive() throws IOException {
		try {
			synchronized (this) {
				while (buffer == null) {
					wait(100);
				}
			}
			log.trace(this + " Received: " + NfcUtils.convertBinToASCII(buffer));
			byte[] b = buffer;
			buffer = null;
			return b;
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void send(byte[] data) throws IOException {
		log.trace(this + " Sending: " + NfcUtils.convertBinToASCII(data));
		other.onMessage(data);
		try {
			Thread.sleep(50);
		}
		catch (InterruptedException e) {
		}
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public byte[] getGeneralBytes() {
		return generalBytes;
	}

	@Override
	public String toString() {
		return super.toString() + " " + (initiator ? "I" : "T");
	}

}

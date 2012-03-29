package org.nfctools.ndefpush;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.nfctools.ndef.Record;

public class OutgoingNdefMessage {

	private ByteArrayInputStream outgoingBuffer;
	private Collection<Record> ndefRecords = new ArrayList<Record>();
	private Collection<NdefPushFinishListener> finishListenerList = new ArrayList<NdefPushFinishListener>();

	public void addWaitingNdefMessage(WaitingNdefMessage waitingNdefMessage) {
		ndefRecords.addAll(waitingNdefMessage.getNdefRecords());
		if (waitingNdefMessage.getFinishListener() != null)
			finishListenerList.add(waitingNdefMessage.getFinishListener());
	}

	public void compile() {
		outgoingBuffer = new ByteArrayInputStream(NdefPushProtocol.toByteArray(ndefRecords));
	}

	public int getAvailableBytes() {
		return outgoingBuffer.available();
	}

	public void readNextBuffer(byte[] buffer) {
		try {
			outgoingBuffer.read(buffer);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void notifyFinishListenerSuccess() {
		for (NdefPushFinishListener finishListener : finishListenerList) {
			try {
				finishListener.onNdefPushFinish();
			}
			catch (Throwable e) {
			}
		}
	}

	public void notifyFinishListenerFailure() {
		for (NdefPushFinishListener finishListener : finishListenerList) {
			try {
				finishListener.onNdefPushFailed();
			}
			catch (Throwable e) {
			}
		}
	}

}

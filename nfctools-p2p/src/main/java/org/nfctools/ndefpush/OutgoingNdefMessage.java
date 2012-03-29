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

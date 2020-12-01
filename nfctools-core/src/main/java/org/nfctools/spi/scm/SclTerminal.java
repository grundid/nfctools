/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 * Copyright 2020 https://github.com/tontg
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
package org.nfctools.spi.scm;

import java.io.IOException;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import org.nfctools.api.TagListener;
import org.nfctools.api.TagScannerListener;
import org.nfctools.nfcip.NFCIPConnection;
import org.nfctools.scio.AbstractTerminal;
import org.nfctools.scio.TerminalMode;
import org.nfctools.scio.TerminalStatus;
import org.nfctools.spi.acs.AbstractTerminalTagScanner;
import org.nfctools.spi.acs.InitiatorTerminalTagScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
/**
 * SCL3711 (SCM Microsystems / Identiv) reader
 *
 * @see https://support.identiv.com/scl3711/
 */
public class SclTerminal extends AbstractTerminal {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private Scl3711 scl3711;
    private Thread scanningThread;
    private AbstractTerminalTagScanner tagScanner;

    @Override
    public boolean canHandle(String terminalName) {
        return terminalName.contains("SCL3711");
    }

    @Override
    public void registerTagListener(TagListener tagListener) {
        tagScanner.setTagListener(tagListener);
    }

    @Override
    public void setMode(TerminalMode terminalMode, TagScannerListener tagScannerListener) {
        if (TerminalMode.INITIATOR.equals(terminalMode)) {
            tagScanner = new InitiatorTerminalTagScanner(cardTerminal, tagScannerListener);
        } else {
            throw new UnsupportedOperationException("terminal target mode not supported for now");
        }
        scanningThread = new Thread(tagScanner);
        scanningThread.setDaemon(true);
    }

    @Override
    public void startListening() {
        scanningThread.start();
    }

    @Override
    public void stopListening() {
        scanningThread.interrupt();
    }

    @Override
    public void initInitiatorDep() throws IOException {
        while (!Thread.interrupted()) {
            try {
                Card card = cardTerminal.connect("direct");
                scl3711 = new Scl3711(card);
                notifyStatus(TerminalStatus.WAITING);
                log.info("Waiting...");
                try {
                    Scl3711NfcipManager nfcipManager = new Scl3711NfcipManager(scl3711);
                    NFCIPConnection nfcipConnection = nfcipManager.connectAsInitiator();
                    handleNfcipConnection(nfcipConnection);
                } catch (IOException ex) {
                    log.error("NFCIP exception", ex);
                } finally {
                    log.info("Disconnect from card");
                    card.disconnect(true);
                    notifyStatus(TerminalStatus.DISCONNECTED);
                }
            } catch (CardException e) {
                throw new IOException(e);
            }
        }
    }

    @Override
    public void initTargetDep() throws IOException {
        log.warn("Target mode not supported yet. Using initiator...");
        initInitiatorDep();
    }

    @Override
    protected void handleNfcipConnection(NFCIPConnection nfcipConnection) throws IOException {
        if (nfcipConnection != null && nfcipConnectionListener != null) {
            notifyStatus(TerminalStatus.CONNECTED);
            nfcipConnectionListener.onConnection(nfcipConnection);
        }
    }
}

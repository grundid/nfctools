package org.nfctools.com;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialPortEventListenerImpl implements SerialPortEventListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void serialEvent(SerialPortEvent ev) {
		log.info(ev.toString());
	}
}

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
package org.nfctools.llcp.pdu;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.nfctools.llcp.parameter.LinkTimeOut;
import org.nfctools.llcp.parameter.Miux;
import org.nfctools.llcp.parameter.ReceiveWindowSize;
import org.nfctools.llcp.parameter.ServiceName;
import org.nfctools.llcp.parameter.Version;
import org.nfctools.llcp.parameter.WellKnownServiceList;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.wkt.records.TextRecord;
import org.nfctools.ndefpush.NdefPushProtocol;
import org.nfctools.utils.NfcUtils;

public class PduParserTest {

	private PduDecoder pduDecoder = new PduDecoder();

	@Test
	public void testDecodeConnect() throws Exception {
		byte[] data = { 0x05, 0x21, 0x06, 0x0F, 0x63, 0x6F, 0x6D, 0x2E, 0x61, 0x6E, 0x64, 0x72, 0x6F, 0x69, 0x64, 0x2E,
				0x6E, 0x70, 0x70 };

		Connect connect = (Connect)pduDecoder.decode(data);
		assertEquals(1, connect.getDestinationServiceAccessPoint());
		assertEquals(33, connect.getSourceServiceAccessPoint());

		Object[] parameter = connect.getParameter();
		assertEquals(1, parameter.length);
		ServiceName serviceName = (ServiceName)parameter[0];
		assertEquals("com.android.npp", serviceName.getName());

	}

	@Test
	public void testDecodeInformation() throws Exception {
		byte[] data = { 0x07, 0x21, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x17, (byte)0xD1, 0x01,
				0x13, 0x54, 0x02, 0x65, 0x6E, 0x4E, 0x44, 0x45, 0x46, 0x20, 0x50, 0x75, 0x73, 0x68, 0x20, 0x53, 0x61,
				0x6D, 0x70, 0x6C, 0x65 };

		byte[] ndeforg = { (byte)0xD1, 0x01, 0x13, 0x54, 0x02, 0x65, 0x6E, 0x4E, 0x44, 0x45, 0x46, 0x20, 0x50, 0x75,
				0x73, 0x68, 0x20, 0x53, 0x61, 0x6D, 0x70, 0x6C, 0x65 };

		Information information = (Information)pduDecoder.decode(data);

		List<byte[]> ndefMessages = NdefPushProtocol.parse(information.getServiceDataUnit());

		for (byte[] ndef : ndefMessages) {
			TextRecord record = NdefContext.getNdefMessageDecoder().decodeToRecord(ndef);
			assertEquals("NDEF Push Sample", record.getText());

		}

	}

	@Test
	public void testEncodeConnectComplete() throws Exception {
		byte[] encode = pduDecoder.encode(new ConnectComplete(33, 1, Collections.emptyList()));

		byte[] expected = { (byte)0x85, (byte)0x81 };

		assertArrayEquals(expected, encode);

	}

	@Test
	public void testDecodeConnectComplete() throws Exception {
		byte[] pdu = { (byte)0x85, (byte)0x90, 0x02, 0x02, 0x00, 0x78 };

		AbstractProtocolDataUnit protocolDataUnit = pduDecoder.decode(pdu);

		assertTrue(protocolDataUnit instanceof ConnectComplete);
	}

	@Test
	public void testDecodeAllParams() throws Exception {
		byte[] data = { 0x01, 0x01, 0x10, 0x02, 0x02, 0x03, (byte)0xff, 0x03, 0x02, (byte)0xff, (byte)0xff, 0x04, 0x01,
				(byte)0xff, 0x06, 0x0F, 0x63, 0x6F, 0x6D, 0x2E, 0x61, 0x6E, 0x64, 0x72, 0x6F, 0x69, 0x64, 0x2E, 0x6E,
				0x70, 0x70 };

		Object[] parameter = pduDecoder.decodeParameter(data, 0);
		int pId = 0;

		Version version = (Version)parameter[pId++];
		assertEquals(0, version.getMinor());
		assertEquals(1, version.getMajor());

		Miux miux = (Miux)parameter[pId++];
		assertEquals(1023, miux.getValue());

		WellKnownServiceList wks = (WellKnownServiceList)parameter[pId++];
		assertEquals(0xffff, wks.getValue());

		LinkTimeOut lto = (LinkTimeOut)parameter[pId++];
		assertEquals(255, lto.getValue());

		ServiceName serviceName = (ServiceName)parameter[pId++];
		assertEquals("com.android.npp", serviceName.getName());

	}

	@Test
	public void testDecodeAllParamsWindows8() throws Exception {
		byte[] data = NfcUtils.convertASCIIToBin("052002020380050105060F75726E3A6E66633A736E3A736E6570");

		AbstractProtocolDataUnit dataUnit = pduDecoder.decode(data);
		assertNotNull(dataUnit);
		assertTrue(dataUnit instanceof Connect);
		Connect connect = (Connect)dataUnit;

		Object[] parameter = connect.getParameter();
		assertEquals(3, parameter.length);

		assertEquals(896, ((Miux)parameter[0]).getValue());
		assertEquals(5, ((ReceiveWindowSize)parameter[1]).getSize());
		assertEquals("urn:nfc:sn:snep", ((ServiceName)parameter[2]).getName());

	}

	@Test
	public void testEncodeParams() throws Exception {
		Version version = new Version(1, 1);
		byte[] bs = pduDecoder.encodeParameter(new Object[] { version });
		assertArrayEquals(new byte[] { 0x01, 0x01, 0x11 }, bs);
	}
}

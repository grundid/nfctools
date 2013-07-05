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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.nfctools.llcp.parameter.LinkTimeOut;
import org.nfctools.llcp.parameter.Miux;
import org.nfctools.llcp.parameter.Option;
import org.nfctools.llcp.parameter.ReceiveWindowSize;
import org.nfctools.llcp.parameter.ServiceName;
import org.nfctools.llcp.parameter.Version;
import org.nfctools.llcp.parameter.WellKnownServiceList;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PduDecoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	public AbstractProtocolDataUnit decode(byte[] pduData) {

		int destination = (pduData[0] >> 2) & 0x3f;
		int source = pduData[1] & 0x3f;
		int pduType = ((pduData[0] & 0x03) << 2) | ((pduData[1] & 0xc0) >> 6);

		switch (pduType) {
			case PduConstants.PDU_CONNECT:
				return new Connect(destination, source, decodeParameter(pduData, 2));
			case PduConstants.PDU_CONNECT_COMPLETE:
				return new ConnectComplete(destination, source, decodeParameter(pduData, 2));
			case PduConstants.PDU_DISCONNECT:
				return new Disconnect(destination, source);
			case PduConstants.PDU_DISCONNECTED_MODE:
				int reason = pduData[2];
				return new DisconnectedMode(destination, source, reason);
			case PduConstants.PDU_INFORMATION:
				int received = pduData[2] & 0x0f;
				int send = (pduData[2] >>> 4) & 0x0f;
				byte[] informationData = new byte[pduData.length - 3];
				System.arraycopy(pduData, 3, informationData, 0, informationData.length);
				return new Information(destination, source, received, send, informationData);
			case PduConstants.PDU_PARAMETER_EXCHANGE:
				return new ParameterExchange(destination, source, decodeParameter(pduData, 2));
			case PduConstants.PDU_RECEIVE_READY:
				int receivedReady = pduData[2] & 0x0f;
				return new ReceiveReady(destination, source, receivedReady);
			case PduConstants.PDU_SYMMETRY:
				return new Symmetry();
			case PduConstants.PDU_UNNUMBERED_INFORMATION:
				byte[] unnumberedData = new byte[pduData.length - 2];
				System.arraycopy(pduData, 2, unnumberedData, 0, unnumberedData.length);
				return new UnnumberedInformation(destination, source, unnumberedData);
			case PduConstants.PDU_AGGREGATED_FRAME:
				int currIndex = 2;
				ArrayList<AbstractProtocolDataUnit> frames = new ArrayList<AbstractProtocolDataUnit>();				
				for(int i = currIndex; i+2 <pduData.length;)
				{
					short framelenght =  ByteBuffer.wrap(new byte[]{pduData[i],pduData[i+1]}).getShort();
					byte[] currFrame = new byte[framelenght];
					System.arraycopy(pduData, i+2, currFrame, 0, framelenght);					
					frames.add(this.decode(currFrame));
					i=i+2+framelenght;
				}				
				AbstractProtocolDataUnit[] resultantFrames = new AbstractProtocolDataUnit[frames.size()];
				for(int i = 0; i < frames.size();i++)
					resultantFrames[i] = frames.get(i);				
				return new AggregatedFrame(resultantFrames);
			case PduConstants.PDU_RECEIVE_NOT_READY:
			case PduConstants.PDU_FRAME_REJECT:
				throw new UnsupportedOperationException("PDU TYPE: " + pduType);
			default:
				throw new RuntimeException("unknown pdu type: " + pduType);
		}
	}

	public byte[] encode(AbstractProtocolDataUnit protocolDataUnit) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (protocolDataUnit instanceof ConnectComplete) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_CONNECT_COMPLETE);
			appendParameter(baos, (AbstractParameterProtocolDataUnit)protocolDataUnit);
		}
		else if (protocolDataUnit instanceof Connect) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_CONNECT);
			appendParameter(baos, (AbstractParameterProtocolDataUnit)protocolDataUnit);
		}
		else if (protocolDataUnit instanceof Disconnect) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_DISCONNECT);
		}
		else if (protocolDataUnit instanceof DisconnectedMode) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_DISCONNECTED_MODE);
			baos.write(((DisconnectedMode)protocolDataUnit).getReason());
		}
		else if (protocolDataUnit instanceof Information) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_INFORMATION);
			appendSequence(baos, (AbstractSequenceProtocolDataUnit)protocolDataUnit);
			appendData(baos, ((Information)protocolDataUnit).getServiceDataUnit());
		}
		else if (protocolDataUnit instanceof ParameterExchange) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_PARAMETER_EXCHANGE);
			appendParameter(baos, (AbstractParameterProtocolDataUnit)protocolDataUnit);
		}
		else if (protocolDataUnit instanceof ReceiveReady) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_RECEIVE_READY);
			appendSequence(baos, (AbstractSequenceProtocolDataUnit)protocolDataUnit);
		}
		else if (protocolDataUnit instanceof Symmetry) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_SYMMETRY);
		}
		else if (protocolDataUnit instanceof UnnumberedInformation) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_UNNUMBERED_INFORMATION);
			appendData(baos, ((UnnumberedInformation)protocolDataUnit).getServiceDataUnit());
		}
		else if (protocolDataUnit instanceof AggregatedFrame) {
			appendHeader(baos, protocolDataUnit, PduConstants.PDU_AGGREGATED_FRAME);
			for(int i = 0 ; i < ((AggregatedFrame)protocolDataUnit).getInnerFrames().length;i++)
			{
				byte[] currFrame = this.encode(((AggregatedFrame)protocolDataUnit).getInnerFrames()[i]);	
				
				byte[] currFrameLenght = ByteBuffer.allocate(2).putShort((short)currFrame.length).array();
				
				appendData(baos, currFrameLenght);
				appendData(baos, currFrame);
			}			
		}
		return baos.toByteArray();
	}

	private void appendData(ByteArrayOutputStream baos, byte[] data) {
		try {
			baos.write(data);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void appendSequence(ByteArrayOutputStream baos, AbstractSequenceProtocolDataUnit protocolDataUnit) {
		int sequence = (protocolDataUnit.getSend() & 0x0f) << 4 | (protocolDataUnit.getReceived() & 0x0f);
		baos.write(sequence);
	}

	private void appendParameter(ByteArrayOutputStream baos, AbstractParameterProtocolDataUnit protocolDataUnit) {
		appendData(baos, encodeParameter(protocolDataUnit.getParameter()));
	}

	protected void appendHeader(ByteArrayOutputStream baos, AbstractProtocolDataUnit protocolDataUnit, int pduType) {
		int b1 = ((protocolDataUnit.getDestinationServiceAccessPoint() & 0x3f) << 2) | (pduType >> 2);
		int b2 = ((pduType & 0x03) << 6) | (protocolDataUnit.getSourceServiceAccessPoint() & 0x3f);
		baos.write(b1);
		baos.write(b2);
	}

	public Object[] decodeParameter(byte[] pduData) {
		return decodeParameter(pduData, 0);
	}

	public Object[] decodeParameter(byte[] pduData, int offset) {
		List<Object> params = new ArrayList<Object>();
		while (offset < pduData.length) {
			switch (pduData[offset]) {
				case PduConstants.PARAM_VERSION:
					byte major = (byte)((pduData[offset + 2] >> 4) & 0x0F);
					byte minor = (byte)(pduData[offset + 2] & 0x0F);
					params.add(new Version(major, minor));
					break;
				case PduConstants.PARAM_SN:
					String serviceName = new String(pduData, offset + 2, pduData[offset + 1]);
					params.add(new ServiceName(serviceName));
					break;
				case PduConstants.PARAM_MIUX:
					int miux = (pduData[offset + 2] & 0x03) << 8 | (pduData[offset + 3] & 0xFF);
					params.add(new Miux(miux));
					break;
				case PduConstants.PARAM_WKS:
					int wks = (pduData[offset + 2] & 0xFF) << 8 | (pduData[offset + 3] & 0xFF);
					params.add(new WellKnownServiceList(wks));
					break;
				case PduConstants.PARAM_LTO:
					params.add(new LinkTimeOut(pduData[offset + 2] & 0xFF));
					break;
				case PduConstants.PARAM_RW:
					params.add(new ReceiveWindowSize(pduData[offset + 2] & 0x0F));
					break;
				case PduConstants.PARAM_OPT:
					params.add(new Option(pduData[offset + 2] & 0x01));
					break;

				default:
					throw new IllegalArgumentException("unknown code " + pduData[offset] + " at position " + offset
							+ ". [" + NfcUtils.convertBinToASCII(pduData) + "]");
			}
			offset += 2 + pduData[offset + 1];
		}
		return params.toArray();
	}

	public byte[] encodeParameter(Object[] parameter) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		for (Object object : parameter) {
			if (object instanceof Version) {
				baos.write(PduConstants.PARAM_VERSION);
				baos.write(0x01);
				byte versionByte = createVersionByte((Version)object);
				baos.write(versionByte);
			}
			else if (object instanceof ServiceName) {
				byte[] serviceName = ((ServiceName)object).getName().getBytes();
				baos.write(PduConstants.PARAM_SN);
				baos.write(serviceName.length);
				appendData(baos, serviceName);
			}
			else if (object instanceof Miux) {
				Miux miux = (Miux)object;
				baos.write(PduConstants.PARAM_MIUX);
				baos.write(2);
				baos.write((miux.getValue() >> 8) & 0x03);
				baos.write(miux.getValue() & 0xff);
			}
		}

		return baos.toByteArray();
	}

	private byte createVersionByte(Version version) {
		if (version.getMajor() > 15 | version.getMajor() < 1 | version.getMinor() > 15 | version.getMinor() < 0)
			throw new IllegalArgumentException("Version out of range");
		byte versionByte = (byte)((version.getMajor() << 4) | version.getMinor());
		return versionByte;
	}
}

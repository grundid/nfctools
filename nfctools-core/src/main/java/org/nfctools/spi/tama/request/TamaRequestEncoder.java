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
package org.nfctools.spi.tama.request;

import org.nfctools.spi.tama.TamaException;

public class TamaRequestEncoder {

	public <T> byte[] encodeMessage(T request) throws TamaException {
		if (request instanceof DataExchangeReq)
			return inDataExchange((DataExchangeReq)request);
		else if (request instanceof JumpForDepReq)
			return inJumpForDep((JumpForDepReq)request);
		else if (request instanceof InitTamaTargetReq)
			return tgInitTamaTarget((InitTamaTargetReq)request);
		else if (request instanceof GetDepDataReq)
			return tgGetDEPData();
		else if (request instanceof SetMetaDepDataReq)
			return tgSetMetaDEPData((SetMetaDepDataReq)request);
		else if (request instanceof SetDepDataReq)
			return tgSetDEPData((SetDepDataReq)request);
		else if (request instanceof GetFirmwareVersionReq)
			return getFirmwareVersion();
		else if (request instanceof GetGeneralStatusReq)
			return getGeneralStatus();
		else if (request instanceof DeselectReq)
			return inDeselect((DeselectReq)request);
		else if (request instanceof SelectReq)
			return inSelect((SelectReq)request);
		else if (request instanceof ReleaseReq)
			return inRelease((ReleaseReq)request);
		else if (request instanceof SetParametersReq)
			return setParameters((SetParametersReq)request);
		else if (request instanceof SetGeneralBytesReq)
			return tgSetGeneralBytes((SetGeneralBytesReq)request);
		else if (request instanceof RfCommunicationReq)
			return rfCommunication((RfCommunicationReq)request);
		else if (request instanceof TgResponseToInitiatorReq)
			return tgResponseToInitiator((TgResponseToInitiatorReq)request);

		throw new TamaException("Unknown request object: " + request.getClass().getName());
	}

	private byte[] inDataExchange(DataExchangeReq request) {
		byte[] buffer = new byte[3 + request.getLength()];
		buffer[0] = (byte)0xD4;
		buffer[1] = (byte)0x40;
		buffer[2] = (byte)request.getTargetId();
		if (request.isMoreInformation())
			buffer[2] |= 0x40;
		System.arraycopy(request.getDataOut(), request.getOffset(), buffer, 3, request.getLength());
		return buffer;
	}

	private byte[] inJumpForDep(JumpForDepReq request) {

		if (!request.isActive() && request.getPassiveInitiatorData() == null
				|| request.getPassiveInitiatorData().length != 5)
			throw new IllegalArgumentException("must set passive initiator data in passive mode");

		// TODO check for 4 bytes in PassiveInitiatorData if baud = 106 and 5 bytes if baud = 212/424
		// TODO check nfcid length 10 bytes

		byte[] buffer = new byte[5
				+ (request.getPassiveInitiatorData() == null ? 0 : request.getPassiveInitiatorData().length)
				+ (request.getNfcId3i() == null ? 0 : request.getNfcId3i().length)
				+ (request.getGeneralBytes() == null ? 0 : request.getGeneralBytes().length)];

		buffer[0] = (byte)0xD4;
		buffer[1] = 0x56;
		buffer[2] = (byte)(request.isActive() ? 0x01 : 0x00);
		buffer[3] = request.getBautRate();
		buffer[4] = (byte)((request.getPassiveInitiatorData() == null ? 0 : 0x01)
				| (request.getNfcId3i() == null ? 0 : 0x02) | (request.getGeneralBytes() == null ? 0 : 0x04));
		int bufPos = 5;
		if (request.getPassiveInitiatorData() != null) {
			System.arraycopy(request.getPassiveInitiatorData(), 0, buffer, bufPos,
					request.getPassiveInitiatorData().length);
			bufPos += request.getPassiveInitiatorData().length;
		}
		if (request.getNfcId3i() != null) {
			System.arraycopy(request.getNfcId3i(), 0, buffer, bufPos, request.getNfcId3i().length);
			bufPos += request.getNfcId3i().length;
		}
		if (request.getGeneralBytes() != null) {
			System.arraycopy(request.getGeneralBytes(), 0, buffer, bufPos, request.getGeneralBytes().length);
			bufPos += request.getGeneralBytes().length;
		}

		return buffer;
	}

	private byte[] tgInitTamaTarget(InitTamaTargetReq request) {
		byte[] buffer = new byte[37 + 1 + 1 + (request.getGeneralBytes() == null ? 0 : request.getGeneralBytes().length)];

		buffer[0] = (byte)0xD4;
		buffer[1] = (byte)0x8C;
		buffer[2] = (byte)((request.isDepOnly() ? 0x02 : 0) | (request.isPassiveOnly() ? 0x01 : 0));
		int bufPos = 3;
		System.arraycopy(request.getMifareParams(), 0, buffer, bufPos, request.getMifareParams().length);
		bufPos += request.getMifareParams().length;
		System.arraycopy(request.getFelicaParams(), 0, buffer, bufPos, request.getFelicaParams().length);
		bufPos += request.getFelicaParams().length;
		System.arraycopy(request.getNfcId3t(), 0, buffer, bufPos, request.getNfcId3t().length);
		bufPos += request.getNfcId3t().length;
		if (request.getGeneralBytes() != null) {
			buffer[bufPos++] = (byte)request.getGeneralBytes().length;
			System.arraycopy(request.getGeneralBytes(), 0, buffer, bufPos, request.getGeneralBytes().length);
			bufPos += request.getGeneralBytes().length;
		}
		else
			buffer[bufPos++] = 0;
		buffer[bufPos] = 0; // No historical bytes
		return buffer;
	}

	private byte[] tgGetDEPData() {
		return new byte[] { (byte)0xD4, (byte)0x86 };
	}

	private byte[] getFirmwareVersion() {
		return new byte[] { (byte)0xD4, (byte)0x02 };
	}

	private byte[] getGeneralStatus() {
		return new byte[] { (byte)0xD4, (byte)0x04 };
	}

	private byte[] tgSetDEPData(SetDepDataReq request) {
		byte[] buffer = new byte[2 + request.getLength()];
		buffer[0] = (byte)0xD4;
		buffer[1] = (byte)0x8E;
		System.arraycopy(request.getDataOut(), request.getOffset(), buffer, 2, request.getLength());
		return buffer;
	}

	private byte[] tgSetMetaDEPData(SetMetaDepDataReq request) {
		byte[] buffer = new byte[2 + request.getLength()];
		buffer[0] = (byte)0xD4;
		buffer[1] = (byte)0x94;
		System.arraycopy(request.getDataOut(), request.getOffset(), buffer, 2, request.getLength());
		return buffer;
	}

	private byte[] tgResponseToInitiator(TgResponseToInitiatorReq request) {
		byte[] buffer = new byte[2 + request.getLength()];
		buffer[0] = (byte)0xD4;
		buffer[1] = (byte)0x90;
		System.arraycopy(request.getDataOut(), request.getOffset(), buffer, 2, request.getLength());
		return buffer;
	}

	private byte[] inDeselect(DeselectReq request) {
		return new byte[] { (byte)0xD4, (byte)0x44, (byte)request.getTargetId() };
	}

	private byte[] inRelease(ReleaseReq request) {
		return new byte[] { (byte)0xD4, (byte)0x52, (byte)request.getTargetId() };
	}

	private byte[] inSelect(SelectReq request) {
		return new byte[] { (byte)0xD4, (byte)0x54, (byte)request.getTargetId() };
	}

	private byte[] setParameters(SetParametersReq request) {
		return new byte[] { (byte)0xD4, (byte)0x12, request.getFlags() };
	}

	private byte[] tgSetGeneralBytes(SetGeneralBytesReq request) {
		byte[] buffer = new byte[2 + request.getGeneralBytes().length];
		buffer[0] = (byte)0xD4;
		buffer[1] = (byte)0x92;
		System.arraycopy(request.getGeneralBytes(), 0, buffer, 2, request.getGeneralBytes().length);
		return buffer;
	}

	private byte[] rfCommunication(RfCommunicationReq request) {
		byte[] buffer = new byte[3 + request.getConfigData().length];
		buffer[0] = (byte)0xd4;
		buffer[1] = 0x32;
		buffer[2] = (byte)request.getConfigItem();
		System.arraycopy(request.getConfigData(), 0, buffer, 3, request.getConfigData().length);
		return buffer;

	}
}

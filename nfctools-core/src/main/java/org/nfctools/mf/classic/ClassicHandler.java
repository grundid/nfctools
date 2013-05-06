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
package org.nfctools.mf.classic;

import java.io.IOException;
import java.util.Arrays;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.mad.Application;
import org.nfctools.utils.NfcUtils;

public class ClassicHandler {

	public static boolean isBlank(MfClassicReaderWriter readerWriter) throws IOException {
		try {
			MemoryLayout memoryLayout = readerWriter.getMemoryLayout();
			for (int sector = 0; sector < memoryLayout.getSectors(); sector++) {
				TrailerBlock trailerBlock = readTrailerBlock(readerWriter, sector, MfClassicConstants.TRANSPORT_KEY);
				if (!Arrays.equals(MfConstants.TRANSPORT_ACCESS_CONDITIONS, trailerBlock.getAccessConditions()))
					return false;
				if (trailerBlock.getGeneralPurposeByte() != MfConstants.TRANSPORT_GPB)
					return false;
			}
			return true;
		}
		catch (MfLoginException e) {
			return false;
		}
	}

	public static boolean isFormattedWritable(Application application, KeyValue keyValue) throws IOException {
		try {
			TrailerBlock trailerBlock = application.readTrailer(keyValue);
			for (int dataArea = 0; dataArea < 3; dataArea++) {
				if (!trailerBlock.canWriteDataBlock(keyValue.getKey(), dataArea))
					return false;
			}
			if (NfcUtils.getLeastSignificantNibble(trailerBlock.getGeneralPurposeByte()) != 0)
				return false;
			return true;
		}
		catch (MfLoginException e) {
			return false;
		}
	}

	public static TrailerBlock createReadWriteDataTrailerBlock() {
		try {
			TrailerBlock trailerBlock = new TrailerBlock();
			trailerBlock.setAccessConditions(MfConstants.NDEF_READ_WRITE_ACCESS_CONDITIONS);
			trailerBlock.setGeneralPurposeByte(MfConstants.NDEF_GPB_V10_READ_WRITE);
			return trailerBlock;
		}
		catch (MfException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("null")
	private static TrailerBlock readTrailerBlock(MfClassicReaderWriter readerWriter, int sector, KeyValue... keys)
			throws IOException {
		MfLoginException lastException = null;
		for (KeyValue keyValue : keys) {
			try {
				MemoryLayout memoryLayout = readerWriter.getMemoryLayout();
				int trailerBlockNumberForSector = memoryLayout.getTrailerBlockNumberForSector(sector);
				MfClassicAccess access = new MfClassicAccess(keyValue, sector, trailerBlockNumberForSector);
				TrailerBlock trailerBlock = (TrailerBlock)readerWriter.readBlock(access)[0];
				return trailerBlock;
			}
			catch (MfLoginException e) {
				lastException = e;
			}
		}
		throw lastException;
	}
}

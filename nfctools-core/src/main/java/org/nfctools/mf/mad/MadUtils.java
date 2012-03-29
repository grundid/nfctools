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
package org.nfctools.mf.mad;

import java.io.IOException;

import org.nfctools.mf.Key;
import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.SimpleMfAccess;
import org.nfctools.mf.block.DataBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.card.MfCard1k;
import org.nfctools.mf.card.MfCard4k;

public class MadUtils {

	/**
	 * Returns the application directory in read-only mode. If a write attempt is made on the application directory an
	 * IllegalStateException is thrown. If the card does not have an application directory a MfException is thrown.
	 * 
	 * @param card
	 * @param readerWriter
	 * @throws IOException
	 */
	public static ApplicationDirectory getApplicationDirectory(MfCard card, MfReaderWriter readerWriter)
			throws IOException {

		return getApplicationDirectory(card, readerWriter, null);
	}

	/**
	 * Returns the application directory in read-write mode. If the card does not have an application directory a
	 * MfException is thrown.
	 * 
	 * @param card
	 * @param readerWriter
	 * @throws IOException
	 */
	public static ApplicationDirectory getApplicationDirectory(MfCard card, MfReaderWriter readerWriter,
			byte[] writeKeyValue) throws IOException {

		SimpleMfAccess simpleMfAccess = new SimpleMfAccess(card, Key.A, MadConstants.DEFAULT_MAD_KEY);

		TrailerBlock madTrailer = (TrailerBlock)readerWriter.readBlock(new MfAccess(simpleMfAccess, 0, card
				.getTrailerBlockNumberForSector(0), 1))[0];

		if ((madTrailer.getGeneralPurposeByte() & MadConstants.GPB_MAD_AVAILABLE) != 0) {
			if ((madTrailer.getGeneralPurposeByte() & MadConstants.GPB_MAD_V1) == MadConstants.GPB_MAD_V1) {
				madTrailer.setKey(Key.A, MadConstants.DEFAULT_MAD_KEY);
				if (writeKeyValue != null)
					madTrailer.setKey(Key.B, writeKeyValue);
				Mad1 mad1 = new Mad1(readerWriter, card, madTrailer);
				mad1.readMad();
				if (writeKeyValue == null)
					mad1.setReadonly(true);
				return mad1;
			}
			else if ((madTrailer.getGeneralPurposeByte() & MadConstants.GPB_MAD_V2) == MadConstants.GPB_MAD_V2) {
				madTrailer.setKey(Key.A, MadConstants.DEFAULT_MAD_KEY);
				if (writeKeyValue != null)
					madTrailer.setKey(Key.B, writeKeyValue);
				Mad2 mad = new Mad2(readerWriter, card, madTrailer);
				mad.readMad();
				if (writeKeyValue == null)
					mad.setReadonly(true);
				return mad;
			}
			else {
				throw new MfException("MAD version not supported");
			}
		}
		else {
			throw new MfException("MAD not available");
		}
	}

	public static boolean hasApplicationDirectory(MfCard card, MfReaderWriter readerWriter) throws IOException {
		try {
			SimpleMfAccess simpleMfAccess = new SimpleMfAccess(card, Key.A, MadConstants.DEFAULT_MAD_KEY);

			TrailerBlock madTrailer = (TrailerBlock)readerWriter.readBlock(new MfAccess(simpleMfAccess, 0, card
					.getTrailerBlockNumberForSector(0), 1))[0];

			return ((madTrailer.getGeneralPurposeByte() & MadConstants.GPB_MAD_AVAILABLE) != 0);
		}
		catch (MfLoginException e) {
			return false;
		}
	}

	public static ApplicationDirectory createApplicationDirectory(MfCard card, MfReaderWriter readerWriter,
			Key createKey, byte[] createKeyValue, byte[] writeKeyValue) throws IOException {
		if (card instanceof MfCard1k) {
			TrailerBlock trailerBlock = new TrailerBlock();
			trailerBlock.setKey(Key.A, MadConstants.DEFAULT_MAD_KEY);
			trailerBlock.setKey(Key.B, writeKeyValue);
			trailerBlock.setAccessConditions(MadConstants.DEFAULT_MAD_ACCESS_CONDITIONS);
			trailerBlock.setGeneralPurposeByte((byte)(MadConstants.GPB_MAD_AVAILABLE | MadConstants.GPB_MAD_V1));

			MfAccess mfAccess = new MfAccess(card, 0, card.getTrailerBlockNumberForSector(0), createKey, createKeyValue);
			readerWriter.writeBlock(mfAccess, trailerBlock);

			MfAccess mfAccessDataBlock = new MfAccess(card, 0, 1, Key.B, writeKeyValue);
			readerWriter.writeBlock(mfAccessDataBlock, new DataBlock(), new DataBlock());

			Mad1 mad1 = new Mad1(readerWriter, card, trailerBlock);
			mad1.writeMad();

			return mad1;
		}

		if (card instanceof MfCard4k) {
			TrailerBlock trailerBlock = new TrailerBlock();
			trailerBlock.setKey(Key.A, MadConstants.DEFAULT_MAD_KEY);
			trailerBlock.setKey(Key.B, writeKeyValue);
			trailerBlock.setAccessConditions(MadConstants.DEFAULT_MAD_ACCESS_CONDITIONS);
			trailerBlock.setGeneralPurposeByte((byte)(MadConstants.GPB_MAD_AVAILABLE | MadConstants.GPB_MAD_V2));

			MfAccess mfAccess = new MfAccess(card, 0, card.getTrailerBlockNumberForSector(0), createKey, createKeyValue);
			readerWriter.writeBlock(mfAccess, trailerBlock);

			MfAccess mfAccessDataBlock = new MfAccess(card, 0, 1, Key.B, writeKeyValue);
			readerWriter.writeBlock(mfAccessDataBlock, new DataBlock(), new DataBlock());

			MfAccess mfAccess2 = new MfAccess(card, 16, card.getTrailerBlockNumberForSector(16), createKey,
					createKeyValue);
			readerWriter.writeBlock(mfAccess2, trailerBlock);

			MfAccess mfAccessDataBlock2 = new MfAccess(card, 16, 1, Key.B, writeKeyValue);
			readerWriter.writeBlock(mfAccessDataBlock2, new DataBlock(), new DataBlock());

			Mad2 mad = new Mad2(readerWriter, card, trailerBlock);
			mad.writeMad();

			return mad;
		}
		throw new RuntimeException("Unsupported card " + card.getClass().getName());
	}

}

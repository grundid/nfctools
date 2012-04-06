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
package org.nfctools.mf;

import java.io.IOException;

import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;

public class MfUtils {

	public static void initTransportConfig(MfReaderWriter readerWriter, MfCard card, int sector, Key key,
			byte[] keyValue) throws IOException {
		TrailerBlock transportTrailer = new TrailerBlock();
		readerWriter.writeBlock(new MfAccess(card, sector, card.getTrailerBlockNumberForSector(sector), key, keyValue),
				transportTrailer);
	}

	public static int getLeastSignificantNibble(byte data) {
		return data & 0x0F;
	}

	public static int getMostSignificantNibble(byte data) {
		return (data & 0xF0) >> 4;
	}

	public static byte encodeNibbles(int mostSignificantNibble, int leastSignificantNibble) {
		return (byte)((mostSignificantNibble & 0x0F) << 4 | (leastSignificantNibble & 0x0F));
	}
}

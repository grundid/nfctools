package org.nfctools.mf.tools;

import java.io.IOException;

import org.nfctools.mf.Key;
import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.MfUtils;
import org.nfctools.mf.block.DataBlock;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.utils.NfcUtils;

public class CardCleaner extends AbstractCardTool {

	@Override
	public void doWithCard(MfCard card, MfReaderWriter readerWriter) throws IOException {
		DataBlock emptyDataBlock = new DataBlock();

		for (int sectorId = 0; sectorId < card.getSectors(); sectorId++) {
			TrailerBlock trailerBlock = readTrailerBlock(card, readerWriter, sectorId);
			if (trailerBlock != null) {
				Key keyToWrite = trailerBlock.isKeyBReadable() ? Key.A : Key.B;
				for (byte[] key : knownKeys) {
					try {
						MfUtils.initTransportConfig(readerWriter, card, sectorId, keyToWrite, key);
						for (int blockId = 0; blockId < card.getBlocksPerSector(sectorId); blockId++) {
							if (!card.isTrailerBlock(sectorId, blockId) && !(blockId == 0 && sectorId == 0)) {
								log.debug("Cleaning S" + sectorId + "|B" + blockId);
								MfAccess mfAccess = new MfAccess(card, sectorId, blockId, Key.A,
										MfConstants.TRANSPORT_KEY);
								readerWriter.writeBlock(mfAccess, emptyDataBlock);
							}
						}
						log.debug("Sector " + sectorId + " clear with key: " + NfcUtils.convertBinToASCII(key));
						break;
					}
					catch (MfLoginException e) {
						log.debug("Cannot clear sector: " + sectorId + " with key " + NfcUtils.convertBinToASCII(key));
					}
				}
			}
			else {
				log.debug("Cannot read trailer block in sector: " + sectorId);
			}
		}
	}

	private TrailerBlock readTrailerBlock(MfCard card, MfReaderWriter readerWriter, int sectorId) throws IOException {
		for (byte[] key : knownKeys) {
			try {
				MfAccess mfAccess = new MfAccess(card, sectorId, card.getTrailerBlockNumberForSector(sectorId), Key.A,
						key);
				MfBlock[] readBlock = readerWriter.readBlock(mfAccess);
				return (TrailerBlock)readBlock[0];
			}
			catch (MfLoginException e) {
			}
		}
		return null;
	}

}

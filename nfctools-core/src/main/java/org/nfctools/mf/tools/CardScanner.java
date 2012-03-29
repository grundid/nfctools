package org.nfctools.mf.tools;

import java.io.IOException;

import org.nfctools.mf.Key;
import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.utils.NfcUtils;

public class CardScanner extends AbstractCardTool {

	@Override
	public void doWithCard(MfCard card, MfReaderWriter readerWriter) throws IOException {
		for (int sectorId = 0; sectorId < card.getSectors(); sectorId++) {
			for (byte[] key : knownKeys) {
				try {
					MfBlock[] mfBlock = readerWriter.readBlock(new MfAccess(card, sectorId, 0, card
							.getBlocksPerSector(sectorId), Key.A, key));
					for (int blockId = 0; blockId < mfBlock.length; blockId++) {
						System.out.println("S" + sectorId + "|B" + blockId + " Key: " + NfcUtils.convertBinToASCII(key)
								+ " " + mfBlock[blockId]);
					}
					break;
				}
				catch (MfLoginException e) {
					log.info("Cannot read sector: " + sectorId + " with key " + NfcUtils.convertBinToASCII(key));
				}
			}
		}
	}
}

package org.nfctools.mf.block;

import org.nfctools.mf.MfException;
import org.nfctools.mf.card.MfCard;

public class BlockResolver {

	public MfBlock resolveBlock(MfCard card, int sectorId, int blockId, byte[] data) throws MfException {
		if (card.isTrailerBlock(sectorId, blockId))
			return new TrailerBlock(data);

		if ((sectorId == 0) && (blockId == 0))
			return new ManufacturerBlock(data);

		if (ValueBlock.isValidValueBlock(data)) {
			return new ValueBlock(data);
		}
		return new DataBlock(data);
	}
}

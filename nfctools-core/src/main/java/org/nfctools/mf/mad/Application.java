package org.nfctools.mf.mad;

import java.io.IOException;

import org.nfctools.mf.Key;
import org.nfctools.mf.block.TrailerBlock;

public interface Application {

	byte[] getApplicationId();

	int getAllocatedSize();

	byte[] read(Key key, byte[] keyValue) throws IOException;

	void write(Key key, byte[] keyValue, byte[] content) throws IOException;

	void updateTrailer(Key key, byte[] keyValue, TrailerBlock trailerBlock) throws IOException;
}

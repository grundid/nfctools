package org.nfctools.mf.classic;

import java.io.IOException;

import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.mad.ApplicationDirectory;
import org.nfctools.mf.mad.MadKeyConfig;

public interface MfClassicReaderWriter {

	MfBlock[] readBlock(MfClassicAccess access) throws IOException;

	void writeBlock(MfClassicAccess access, MfBlock... mfBlock) throws IOException;

	MemoryLayout getMemoryLayout();

	boolean hasApplicationDirectory() throws IOException;

	ApplicationDirectory createApplicationDirectory(MadKeyConfig keyConfig) throws IOException;

	ApplicationDirectory getApplicationDirectory() throws IOException;

	ApplicationDirectory getApplicationDirectory(MadKeyConfig keyConfig) throws IOException;
}

package org.nfctools.mf.mad;

import java.io.IOException;

import org.nfctools.mf.block.TrailerBlock;

public interface ApplicationDirectory {

	int getGeneralPurposeByte();

	int getInfoByte();

	int getMaxContinousSize();

	int getVersion();

	boolean isFree(int aidSlot);

	int getNumberOfSlots();

	Application createApplication(ApplicationId aId, int sizeToAllocate, byte[] writeKeyValue, TrailerBlock trailerBlock)
			throws IOException;

	Application openApplication(ApplicationId aId) throws IOException;

	void deleteApplication(ApplicationId aId, byte[] writeKeyValue, TrailerBlock trailerBlock) throws IOException;

	boolean isReadonly();

	boolean hasApplication(ApplicationId aId) throws IOException;
}

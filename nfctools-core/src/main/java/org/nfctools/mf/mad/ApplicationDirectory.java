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

	void makeReadOnly() throws IOException;
}

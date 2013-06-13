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

import org.nfctools.api.TagInfoReader;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.mad.ApplicationDirectory;
import org.nfctools.mf.mad.MadKeyConfig;

public interface MfClassicReaderWriter extends TagInfoReader {

	MfBlock[] readBlock(MfClassicAccess access) throws IOException;

	void writeBlock(MfClassicAccess access, MfBlock... mfBlock) throws IOException;

	MemoryLayout getMemoryLayout();

	boolean hasApplicationDirectory() throws IOException;

	ApplicationDirectory createApplicationDirectory(MadKeyConfig keyConfig) throws IOException;

	ApplicationDirectory getApplicationDirectory() throws IOException;

	ApplicationDirectory getApplicationDirectory(MadKeyConfig keyConfig) throws IOException;
}

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
import org.nfctools.mf.classic.KeyValue;

public interface Application {

	byte[] getApplicationId();

	int getAllocatedSize();

	byte[] read(KeyValue keyValue) throws IOException;

	void write(KeyValue keyValue, byte[] content) throws IOException;

	void updateTrailer(KeyValue keyValue, TrailerBlock trailerBlock) throws IOException;

	TrailerBlock readTrailer(KeyValue keyValue) throws IOException;

	void makeReadOnly(KeyValue keyValue) throws IOException;

	ApplicationDirectory getApplicationDirectory();
}

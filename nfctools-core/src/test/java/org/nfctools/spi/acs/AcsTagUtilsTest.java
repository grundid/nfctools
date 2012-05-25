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
package org.nfctools.spi.acs;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nfctools.api.TagType;
import org.nfctools.utils.NfcUtils;

public class AcsTagUtilsTest {

	@Test
	public void testIdentifyTagType() throws Exception {
		assertEquals(TagType.NFCIP,
				AcsTagUtils.identifyTagType(NfcUtils.convertASCIIToBin("804F0CA00000030603FF4000000000")));
	}
}

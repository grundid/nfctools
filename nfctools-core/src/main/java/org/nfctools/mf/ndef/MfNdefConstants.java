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
package org.nfctools.mf.ndef;

import org.nfctools.mf.MfConstants;
import org.nfctools.mf.mad.ApplicationId;

@Deprecated
public class MfNdefConstants {

	public static final ApplicationId NDEF_APP_ID = new ApplicationId(MfConstants.NDEF_APPLICATION_CODE,
			MfConstants.NDEF_FUNCTION_CLUSTER_CODE);
}

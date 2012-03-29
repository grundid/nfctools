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
package org.nfctools.llcp;

import org.nfctools.llcp.parameter.Miux;
import org.nfctools.llcp.parameter.ServiceName;

public class LlcpUtils {

	public static String getServiceNameFromParameters(Object[] parameters) {
		if (parameters != null) {
			for (Object parameter : parameters) {
				if (parameter instanceof ServiceName) {
					return ((ServiceName)parameter).getName();
				}
			}
		}
		return null;
	}

	public static int getMiuExtension(Object[] parameters) {
		if (parameters != null) {
			for (Object parameter : parameters) {
				if (parameter instanceof Miux) {
					return ((Miux)parameter).getValue();
				}
			}
		}
		return 0;
	}
}

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
package org.nfctools.ndef.wkt.records;

public enum Action {
	DEFAULT_ACTION(0), SAVE_FOR_LATER(1), OPEN_FOR_EDITING(2);

	private int value;

	private Action(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Action getActionByValue(int value) {
		for (Action possibleAction : Action.values()) {
			if (value == possibleAction.getValue()) {
				return possibleAction;
			}
		}
		throw new RuntimeException("unkown action value (" + value + ")");
	}
}
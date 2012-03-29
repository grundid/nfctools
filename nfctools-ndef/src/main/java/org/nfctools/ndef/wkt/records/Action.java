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
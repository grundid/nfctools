package org.nfctools.spi.acs;

import java.util.Arrays;

import org.nfctools.mf.classic.KeyValue;
import org.nfctools.mf.classic.MfClassicAccess;

public class LoginKeyHandler {

	/** current logged in key */
	private KeyValue authenticatedKeyValue;
	/** current logged in sector */
	private int authenticatedSector = -1;
	/** keys loaded into the reader */
	private MfClassicAccess[] loadedAuthenticationKeys = new MfClassicAccess[2];
	/**
	 * next key position to be loaded. Rotate the key loading so that we always have the two last keys loaded into the
	 * reader
	 */
	private int nextAuthenticationKeyPosition = 0;

	public void resetCurrentKeys() {
		authenticatedKeyValue = null;
		authenticatedSector = -1;
	}

	public void setSuccessfulLogin(MfClassicAccess access) {
		authenticatedKeyValue = access.getKeyValue();
		authenticatedSector = access.getSector();
	}

	public void rememberKey(MfClassicAccess access) {
		// this key is not loaded, load it into reader now
		// use rotating key location so that the last two keys are the ones loaded into the reader
		int index = getNextKeyPosition();
		loadedAuthenticationKeys[index] = access;
		nextAuthenticationKeyPosition++;
	}

	public int getNextKeyPosition() {
		return nextAuthenticationKeyPosition % loadedAuthenticationKeys.length;
	}

	public int getPreviouslyLoadedKeyIndex(MfClassicAccess access) {
		int index = -1;
		for (int i = 0; i < loadedAuthenticationKeys.length; i++) {
			if (loadedAuthenticationKeys[i] != null) {
				if (Arrays.equals(access.getKeyValue().getKeyValue(), loadedAuthenticationKeys[i].getKeyValue()
						.getKeyValue())) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	public boolean isAlreadyLoggedIn(MfClassicAccess access) {
		return authenticatedSector != -1 && authenticatedSector == access.getSector() && authenticatedKeyValue != null
				&& authenticatedKeyValue.equals(access.getKeyValue());
	}
}

package org.nfctools.api;

public enum TagType {

	/**
	 * Unkown tag
	 */
	UKNOWN,
	/**
	 * Mifare Classic with 1k memory
	 */
	MIFARE_CLASSIC_1K,

	/**
	 * Mifare Classic with 4k memory
	 */
	MIFARE_CLASSIC_4K, MIFARE_ULTRALIGHT, MIFARE_MINI, TOPAZ_JEWEL, FELICA_212K, FELICA_424K,
	/**
	 * Tag with NFCIP (P2P) capabilities
	 */
	NFCIP;
}

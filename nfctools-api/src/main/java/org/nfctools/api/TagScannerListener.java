package org.nfctools.api;

public interface TagScannerListener {

	void onScanningEnded();

	void onScanningFailed(Throwable throwable);

	void onTagHandingFailed(Throwable throwable);
}

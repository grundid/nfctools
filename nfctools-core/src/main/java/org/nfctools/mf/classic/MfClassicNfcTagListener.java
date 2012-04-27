package org.nfctools.mf.classic;

import java.io.IOException;

import org.nfctools.NfcException;
import org.nfctools.api.ApduTag;
import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagType;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.mad.Application;
import org.nfctools.mf.mad.ApplicationDirectory;
import org.nfctools.ndef.NdefListener;
import org.nfctools.spi.acs.AcrMfClassicReaderWriter;

public class MfClassicNfcTagListener implements NfcTagListener {

	private NdefListener ndefListener;

	public MfClassicNfcTagListener() {
	}

	public MfClassicNfcTagListener(NdefListener ndefListener) {
		this.ndefListener = ndefListener;
	}

	@Override
	public boolean canHandle(Tag tag) {
		return tag.getTagType().equals(TagType.MIFARE_CLASSIC_1K) || tag.getTagType().equals(TagType.MIFARE_CLASSIC_4K);
	}

	@Override
	public void handleTag(Tag tag) {
		MemoryLayout memoryLayout = tag.getTagType().equals(TagType.MIFARE_CLASSIC_1K) ? MemoryLayout.CLASSIC_1K
				: MemoryLayout.CLASSIC_4K;
		MfClassicNdefOperations ndefOperations = createNdefOperations((ApduTag)tag, memoryLayout);
		if (ndefListener != null)
			ndefListener.onNdefOperations(ndefOperations);

	}

	protected MfClassicNdefOperations createNdefOperations(ApduTag tag, MemoryLayout memoryLayout) {
		boolean formatted = false;
		boolean writable = false;
		MfClassicReaderWriter readerWriter = new AcrMfClassicReaderWriter(tag, memoryLayout);
		try {
			if (readerWriter.hasApplicationDirectory()) {
				ApplicationDirectory applicationDirectory = readerWriter.getApplicationDirectory();
				if (applicationDirectory.hasApplication(MfConstants.NDEF_APP_ID)) {
					formatted = true;
					Application application = applicationDirectory.openApplication(MfConstants.NDEF_APP_ID);
					writable = ClassicHandler.isFormattedWritable(application);
				}
				else {
					throw new NfcException("Unknown tag contents");
				}
			}
			else {
				if (ClassicHandler.isBlank(readerWriter)) {
					writable = true;
				}
				else
					throw new NfcException("Unknown tag contents");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return new MfClassicNdefOperations(readerWriter, formatted, writable);
	}

}

package org.nfctools.ndef.wkt.decoder;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.records.AbstractWellKnownRecord;

public class GenericWellKnownRecordDecoder extends AbstractRecordDecoder<AbstractWellKnownRecord> {

	public GenericWellKnownRecordDecoder() {
		super(NdefConstants.TNF_WELL_KNOWN);
	}

	@Override
	public boolean canDecode(NdefRecord ndefRecord) {
		if (super.canDecode(ndefRecord)) {
			String type = new String(ndefRecord.getType());
			return NdefContext.getKnownRecordsByType().containsKey(type);
		}
		else
			return false;
	}

	@Override
	protected AbstractWellKnownRecord createRecord(NdefRecord ndefRecord, NdefMessageDecoder messageDecoder) {
		try {
			String type = new String(ndefRecord.getType());
			Class<? extends AbstractWellKnownRecord> recordClass = NdefContext.getKnownRecordsByType().get(type);
			return recordClass.newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

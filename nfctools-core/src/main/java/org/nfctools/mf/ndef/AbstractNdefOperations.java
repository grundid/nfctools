package org.nfctools.mf.ndef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nfctools.mf.tlv.NdefMessageTlv;
import org.nfctools.mf.tlv.Tlv;
import org.nfctools.mf.tlv.TypeLengthValueReader;
import org.nfctools.ndef.NdefContext;
import org.nfctools.ndef.NdefMessage;
import org.nfctools.ndef.NdefMessageDecoder;
import org.nfctools.ndef.NdefOperations;
import org.nfctools.ndef.Record;

public abstract class AbstractNdefOperations implements NdefOperations {

	protected boolean formatted;
	protected boolean writable;
	protected List<Record> lastReadRecords;

	protected AbstractNdefOperations(boolean formatted, boolean writable) {
		this.formatted = formatted;
		this.writable = writable;
	}

	@Override
	public boolean hasNdefMessage() {
		if (lastReadRecords != null && !lastReadRecords.isEmpty())
			return true;

		Collection<Record> ndefMessage = readNdefMessage();
		return !ndefMessage.isEmpty();
	}

	@Override
	public boolean isFormatted() {
		return formatted;
	}

	@Override
	public boolean isWritable() {
		return writable;
	}

	@Override
	public void format() {
		format(new Record[0]);
	}

	@Override
	public void formatReadOnly(Record... records) {
		format(records);
		makeReadOnly();
	}

	protected void assertWritable() {
		if (!writable)
			throw new IllegalStateException("Tag not writable");
	}

	protected void assertFormatted() {
		if (!formatted)
			throw new IllegalStateException("Tag not formatted");
	}

	protected byte[] convertRecordsToBytes(Record[] records) {
		if (records.length == 0)
			return new byte[0];
		else {
			return NdefContext.getNdefMessageEncoder().encode(records);
		}
	}

	protected void convertRecords(TypeLengthValueReader reader) {
		lastReadRecords = new ArrayList<Record>();

		NdefMessageDecoder ndefMessageDecoder = NdefContext.getNdefMessageDecoder();
		while (reader.hasNext()) {
			Tlv tlv = reader.next();
			if (tlv instanceof NdefMessageTlv) {
				NdefMessage ndefMessage = ndefMessageDecoder.decode(((NdefMessageTlv)tlv).getNdefMessage());
				for (Record record : ndefMessageDecoder.decodeToRecords(ndefMessage)) {
					lastReadRecords.add(record);
				}
			}
		}
	}

}

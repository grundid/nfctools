package org.nfctools.ndef.wkt.encoder;

public class Snippet {
	public static final void main(String[] args) {
		for(int i = 0; i < 256; i++) {
			byte b = (byte)i;
			
			int ii = (int)(b & 0xFF);
			
			System.out.println(Byte.toString(b) + " " + Integer.toString(ii));
		}
	}
}


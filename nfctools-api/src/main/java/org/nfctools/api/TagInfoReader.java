package org.nfctools.api;

import java.io.IOException;

public interface TagInfoReader {

	TagInfo getTagInfo() throws IOException;
}

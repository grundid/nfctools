package org.nfctools.api;

public class TagInfo {

	private TagType tagType;
	private byte[] id;

	public TagInfo(TagType tagType, byte[] id) {
		this.tagType = tagType;
		this.id = id;
	}

	public TagType getTagType() {
		return tagType;
	}

	public byte[] getId() {
		return id;
	}
}

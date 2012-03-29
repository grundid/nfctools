package org.nfctools.llcp;

public class AddressPair {

	private int remote;
	private int local;

	public AddressPair(int remote, int local) {
		this.remote = remote;
		this.local = local;
	}

	public int getRemote() {
		return remote;
	}

	public int getLocal() {
		return local;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + local;
		result = prime * result + remote;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressPair other = (AddressPair)obj;
		if (local != other.local)
			return false;
		if (remote != other.remote)
			return false;
		return true;
	}

}

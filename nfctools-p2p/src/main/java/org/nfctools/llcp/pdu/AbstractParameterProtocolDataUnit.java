package org.nfctools.llcp.pdu;


public abstract class AbstractParameterProtocolDataUnit extends AbstractProtocolDataUnit {

	private Object[] parameter;

	protected AbstractParameterProtocolDataUnit(int destinationServiceAccessPoint, int sourceServiceAccessPoint,
			Object... parameter) {
		super(destinationServiceAccessPoint, sourceServiceAccessPoint);
		this.parameter = parameter;
	}

	public Object[] getParameter() {
		return parameter;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (parameter != null) {
			for (Object o : parameter) {
				sb.append(o.toString()).append(" ");
			}
		}

		return super.toString() + " " + sb.toString();
	}

}

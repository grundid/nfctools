package org.nfctools.llcp.pdu;

import org.nfctools.llcp.LlcpConnectionManager;

public class AggregatedFrame extends AbstractProtocolDataUnit {
	
	protected AbstractProtocolDataUnit[] innerFrames;
	
	public AbstractProtocolDataUnit[] getInnerFrames() {
		return innerFrames;
	}

	public void setInnerFrames(AbstractProtocolDataUnit[] innerFrames) {
		this.innerFrames = innerFrames;
	}

	public AggregatedFrame(AbstractProtocolDataUnit[] _innerFrames) {
		super(0,0);		
		innerFrames = _innerFrames;
	}

	@Override
	public AbstractProtocolDataUnit processPdu(LlcpConnectionManager connectionManager) {
		AbstractProtocolDataUnit[] responseFrames = new AbstractProtocolDataUnit[innerFrames.length];
		for(int i = 0; i < innerFrames.length;i++)
		{
			responseFrames[i] = innerFrames[i].processPdu(connectionManager);			
		}
		
		return new AggregatedFrame(responseFrames);
	}
	
}

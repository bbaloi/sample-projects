package com.extemp.semantic.be;

import java.io.Serializable;

import com.tibco.cep.runtime.model.TypeManager;
import com.tibco.cep.runtime.model.event.EventPayload;
import com.tibco.xml.datamodel.XiNode;

public class IntentListPayload implements EventPayload,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private byte [] _payload;
	
	public IntentListPayload(TypeManager.TypeDescriptor descriptor, byte[] buf) throws Exception
	{
		_payload=buf;
	}

	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBytes() throws Exception {
		// TODO Auto-generated method stub
		return _payload;
	}

	@Override
	public void toXiNode(XiNode arg0) {
		// TODO Auto-generated method stub
		
	}

	

}

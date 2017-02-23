package com.tibco.xpdl.bw.vo;

import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings;

public class TransVO 
{
	
	private String fromId;
	private String toId;
	
	public TransVO(String pFrom,String pTo)
	{
		fromId=pFrom;
		toId=pTo;
	}
	public String getFromId() {
		return fromId;
	}
	
	public String getToId() {
		return toId;
	}
	

}

package com.tibco.sip.vo;

import java.util.Date;

public class SIPPacketVo 
{
	private String from;
	private String to;
	private String callerId;
	private String operation;
	private Date date;
	private String srcIp;
	private String destIp;
	private int srcPort;
	private int destPort;
	
	public SIPPacketVo()
	{
		
	}
	
	public SIPPacketVo(String pFrom,String pTo,String pCallerId,String pOp,Date pTstamp,String pSrcIp,int pSrcPort,String pDestIp,int pDestPort)
	{
		
		from = pFrom;
		to = pTo;
		callerId = pCallerId;
		operation=pOp;
		date =pTstamp;
		srcIp = pSrcIp;
		destIp = pDestIp;
		srcPort = pSrcPort;
		destPort = pDestPort;
		System.out.println("built SIPVO");
	}


	public String getCallerId() {
		return callerId;
	}


	public Date getDate() {
		return date;
	}


	public String getDestIp() {
		return destIp;
	}


	public int getDestPort() {
		return destPort;
	}


	public String getFrom() {
		return from;
	}


	public String getOperation() {
		return operation;
	}


	public String getSrcIp() {
		return srcIp;
	}


	public int getSrcPort() {
		return srcPort;
	}


	public String getTo() {
		return to;
	}

}

package com.tibco.sip.util;

import java.util.Date;

public interface Constants 
{
	public String interfacesList = "intefaces.list";
	public String dumpFileName="dump.file";
	//----------------SIP message section------
	//public String sip_def="SIP/2.0";
	public String sip_def="SIP/2.0";
	public String fromSection="From:";
	public String toSection="To:";
	//-----------------RV stuff-------------
	public String rv_service="rv.service";
	public String rv_network="rv.network";
	public String rv_daemon="rv.daemon";
	public String rv_subject="rv.subject";
	//----------RV message fields--------
	public  String rvFrom="From";
	public String rvTo="To";
	public String rvCallerId="CallerID";
	public String rvOperation="Operation";
	public String rvTstamp="CaptureTime";
	public String rvSrcIP="SourceIP";
	public String rvDestIP="DestinationIP";
	public String rvSrcPort="SourcePort";
	public String rvDestPort="DestinationPort";
	
	
	

}

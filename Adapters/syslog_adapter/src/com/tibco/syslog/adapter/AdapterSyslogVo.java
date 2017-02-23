package com.tibco.syslog.adapter;

public class AdapterSyslogVo 
{
	
	private String time;
	private String pID;
	private int severity;
	private int facility;
	private String message;
	private String sourceAddress;
	private String capturePoint;
	
	public AdapterSyslogVo(String pTime, String pPid, int pSev, int pFac, String pMsg,String pSource,String pCapture)
	{
		time=pTime;
		pID=pPid;
		severity=pSev;
		facility=pFac;
		message=pMsg;
		sourceAddress = pSource;
		capturePoint = pCapture;
	}

	public int getFacility() {
		return facility;
	}

	public String getMessage() {
		return message;
	}

	public String getPID() {
		return pID;
	}

	public int getSeverity() {
		return severity;
	}

	public String getTime() {
		return time;
	}

	public String getCapturePoint() {
		return capturePoint;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

}

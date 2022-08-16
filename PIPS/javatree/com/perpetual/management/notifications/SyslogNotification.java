/*
 * LogingNotification.java
 *
 * Created on March 18, 2003, 2:11 PM
 */

package com.perpetual.management.notifications;

import javax.management.Notification;
import java.util.Date;


/**
 *
 * @author  brunob
 */
public class SyslogNotification extends PerpetualManagementNotification
{
    
    private String lTimestamp=null;
    private String lHostName=null;
    private Integer lFacility;
    private Integer lSeverity;
    private Integer lPid;
    private String lProcess=null;
    private String lMessage=null;
    private String lDestHost=null;
    private Integer lDestPort=null;
   /** Creates a new instance of LogingNotification */
    
    public SyslogNotification(String type,Object source,long sequenceNumber,long timeStamp, String message,
                            String pDestHost,Integer pDestPort,String pTimeStamp, String pHostName, Integer pFacility, 
                            Integer pSeverity, Integer pPid,String pProcess,String pSyslogMessage) 
    { 
        super(type,source,sequenceNumber,timeStamp,message);
        lTimestamp = pTimeStamp;
        lHostName=pHostName;
        lFacility=pFacility;
        lSeverity=pSeverity;
        lPid=pPid;
        lProcess=pProcess;
        lMessage=pSyslogMessage;  
        lDestHost = pDestHost;
        lDestPort=pDestPort;
    }    
    public String getHostname()
    {
        return lHostName;
    }
    public Integer getFacility()
    {
        return lFacility;
    }
    public Integer getSeverity()
    {
        return lSeverity;
    }
    public Integer getPid()
    {
        return lPid;
    }
    public String getProcess()
    {
        return lProcess;
    }
    public String getMessage()
    {
        return lMessage;
    }
    public String getSyslogTimestamp()
    {
        return lTimestamp;
    }
    public String getDestHost()
    {
        return lDestHost;
    }
    public Integer getDestPort()
    {
        return lDestPort;
    }
}

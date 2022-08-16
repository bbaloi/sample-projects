/*
 * SyslogRecordVO.java
 *
 * Created on May 16, 2003, 4:04 PM
 */

package com.perpetual.viewer.model.vo;

import java.util.Date;
import com.perpetual.viewer.model.formatters.MessageFormatter;

/**
 *
 * @author  brunob
 */
public class SyslogRecordVO implements java.io.Serializable
{
    private String timestamp=null;
    private String hostname=null;
    private String process=null;
    private String message=null;
    private MessageFormatter lFormatter=null;
    /** Creates a new instance of SyslogRecordVO */
    public SyslogRecordVO(String pTimestamp,String pHostname,String pProcess,String pMessage)
    {
        timestamp = pTimestamp;
        hostname=pHostname;
        process = pProcess;
        message = pMessage;
    }
    public String getTimestamp()
    {
        return timestamp;
    }
    public String getHostname()
    {
        return hostname;
    }
    public String getProcess()
    {
        return process;
    }
    public String getMessage()
    {
        return message;
    }
}

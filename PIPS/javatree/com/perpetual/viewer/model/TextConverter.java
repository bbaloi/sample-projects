/*
 * TextFormatter.java
 *
 * Created on May 16, 2003, 3:59 PM
 */

package com.perpetual.viewer.model;

import com.perpetual.util.PerpetualC2Logger;
import java.util.StringTokenizer;
import java.util.Date;

import com.perpetual.viewer.model.vo.SyslogRecordVO;

/**
 *
 * @author  brunob
 */
public class TextConverter 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( TextConverter.class );
    private StringTokenizer tokenizer=null;
    
    /** Creates a new instance of TextFormatter */
    public TextConverter() {
    }
    public SyslogRecordVO convertRecord(String pSyslogRawLine)
    {
        
        String timestamp = pSyslogRawLine.substring(0,15);
        sLog.debug("Timestamp:"+timestamp);
        //Date date = new Date(timestamp);
        int colonIndex=pSyslogRawLine.lastIndexOf( ':' );    
        //sLog.debug(": is at-"+colonIndex);
        String host_process = pSyslogRawLine.substring(16,colonIndex);
        int spaceIndex=host_process.indexOf(' ');
        String host=host_process.substring(0,spaceIndex);
        sLog.debug("host:"+host);
        String process = host_process.substring(spaceIndex,host_process.length());
        sLog.debug("process:"+process);        
        String message = pSyslogRawLine.substring(colonIndex,pSyslogRawLine.length());
        sLog.debug("message:"+message);
                
        SyslogRecordVO syslogRecord = new SyslogRecordVO(timestamp,host,process,message);
        return syslogRecord;
    }
}

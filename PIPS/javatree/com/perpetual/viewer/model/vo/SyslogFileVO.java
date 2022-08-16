/*
 * SyslogFileVO.java
 *
 * Created on May 22, 2003, 4:42 PM
 */

package com.perpetual.viewer.model.vo;

import java.util.Collection;
/**
 *
 * @author  brunob
 */
public class SyslogFileVO implements java.io.Serializable
{
    
    private Collection syslogRecords=null;;
    private String filename=null;
    /** Creates a new instance of SyslogFileVO */
    
    public SyslogFileVO(Collection pSyslogRecords,String pFileName)
    {
        syslogRecords = pSyslogRecords;
        filename=pFileName;
    }
    public Collection getSyslogRecords()
    {
        return syslogRecords;
    }
    public String getFilename()
    {
        return filename;
    }
}

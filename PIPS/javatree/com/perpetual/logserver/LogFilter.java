package com.perpetual.logserver;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.Constants;

import com.perpetual.util.patternmatcher.IPatternMatcher;
import com.perpetual.util.patternmatcher.IMessagePatternInfo;
import com.perpetual.util.patternmatcher.MessagePatternMatcherEngine;
/**
The default behaviour is that in order to match a log record,
at least one host must match,
a type is only NOT a match if a type was asked for which then must match
 */

public class LogFilter implements java.io.Serializable
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( LogFilter.class );
    
    private IPatternMatcher msgPatternMatcher=null;

	public LogFilter(Map parameterMap)
	{
		m_parameterMap = parameterMap;
                if((m_parameterMap.get(Constants.MessagePattern))!=null)
                    initPatternMatcher();
	}
        private void initPatternMatcher()
        {
            sLog.debug("Initialising MessagePaternMatcherEngine !!!");
            msgPatternMatcher = new MessagePatternMatcherEngine((Collection) m_parameterMap.get(Constants.MessagePattern));
        }
        
	public boolean matches(LogRecord logRecord)
	{
            boolean matches=false;
            
		if (logRecord == null) return false;
		try
		{
                    
                    if(m_parameterMap.get(Constants.MessagePattern)==null)
                    {
                        //sLog.debug("no mesage patternmatching required!");
                        matches = logRecord.matches(m_parameterMap);
                    }
                    else
                    {
                        //sLog.debug("message pattern matching required !");
			boolean tmp = logRecord.matches(m_parameterMap);
                        if(tmp==true && matchMessagePatterns(logRecord.getField("Message").toString()))
                            matches=true;
                        
                    }
			return matches;
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	public boolean typeMatches(String type)
	{
		Object[] types = (Object[])m_parameterMap.get("filetypes");
		if (types == null)
			return true;		// types are optional
		for (int i = 0; i < types.length; i++)
		{
			if (type != null && type.equals(types[i]))
				return true;
		}
		return false;
	}

	public boolean timeRangeMatches(Date startTime, Date endTime)
	{
		Date filterStartTime = (Date)m_parameterMap.get("startTime"), filterEndTime = (Date)m_parameterMap.get("maxTime");
		boolean ret = (endTime == null || filterStartTime.compareTo(endTime) <= 0) && (startTime == null || filterEndTime.compareTo(startTime) >= 0);
//System.out.println(filterStartTime + " " + filterEndTime + "     " + startTime + " " + endTime + "  " + ret);
		return ret;
	}

	public boolean hostMatches(String host)
	{
//System.out.println("????? " + host + " " + m_parameterMap.get("Host"));
		Object[] hosts = (Object[])m_parameterMap.get("Host");
                
		if (hosts == null)
			return false;
		for (int i = 0; i < hosts.length; i++)
		{
			if (host.equals(hosts[i]))
                        {
                            sLog.debug("LogFilter, found matched Host:" + (String)hosts[i]);
				return true;
                        }
		}
		return false;
//		return host.equals(m_parameterMap.get("Host"));
	}
        
        private boolean matchMessagePatterns(String pMessage)
        {
            boolean truth=false;
            
            sLog.debug("About to find if the message has a matching pattern...");
            IMessagePatternInfo patternInfo=msgPatternMatcher.findFirstMatch(pMessage);
                sLog.debug("Message:"+pMessage);                
                if(patternInfo==null) 
                {
                    sLog.debug("the pattern doesn't not match");                    
                }
                else
                {
                    sLog.debug("pattern detected:"+patternInfo.getPatternName());
                    truth=true;
                }
            
            return truth;
        }
        protected void finalize()
        {
            msgPatternMatcher=null;
        }
	private Map m_parameterMap = new HashMap();
}


